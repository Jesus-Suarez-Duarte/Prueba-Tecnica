package com.componentes.Tarjetas.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.Entity.Tarjeta;
import com.componentes.Tarjetas.Entity.Transaccion;
import com.componentes.Tarjetas.Repository.EstadoTransRepository;
import com.componentes.Tarjetas.Repository.TarjetaRepository;
import com.componentes.Tarjetas.Repository.TransaccionRepository;
import com.componentes.Tarjetas.dtos.TransAnulacionDTO;
import com.componentes.Tarjetas.dtos.TransCompraDTO;
import com.componentes.Tarjetas.dtos.TransaccionDTO;
import com.componentes.Tarjetas.dtos.RespAnuTransDTO;
import com.componentes.Tarjetas.mappers.TransaccionMapper;

@Service
public class TransaccionService {
    
    @Autowired
    private TransaccionRepository transaccionRepository;
    
    @Autowired
    private EstadoTransRepository estadoTransRepository;
    
    @Autowired
    private TransaccionMapper transaccionMapper;
    
    @Autowired
    private TarjetaRepository tarjetaRepository;

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("MM/yyyy");
    
    // Obtener todas las transacciones
    public List<TransaccionDTO> getAllTransacciones() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        return transaccionMapper.toDtoList(transacciones);
    }
    
    // Obtener transacción por ID
    public TransaccionDTO getTransaccionById(Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada con ID: " + id));
        return transaccionMapper.toDto(transaccion);
    }
    
	/*
	 * // Guardar nueva transacción public TransaccionDTO
	 * saveTransaccion(TransaccionDTO transaccionDTO) { // Verificar si ya existe if
	 * (transaccionDTO.getIdTrans() != null &&
	 * transaccionRepository.existsById(transaccionDTO.getIdTrans())) { throw new
	 * RuntimeException("Ya existe una transacción con el ID: " +
	 * transaccionDTO.getIdTrans()); }
	 * 
	 * // Obtener EstadoTrans EstadoTrans estadoTrans =
	 * estadoTransRepository.findById(transaccionDTO.getIdEstadoTrans())
	 * .orElseThrow(() -> new
	 * RuntimeException("Estado transacción no encontrado con ID: " +
	 * transaccionDTO.getIdEstadoTrans()));
	 * 
	 * Transaccion transaccion = transaccionMapper.toEntity(transaccionDTO,
	 * estadoTrans); Transaccion savedTransaccion =
	 * transaccionRepository.save(transaccion); return
	 * transaccionMapper.toDto(savedTransaccion); }
	 */
    
    // Actualizar transacción
    public TransaccionDTO updateTransaccion(Long id, TransaccionDTO transaccionDTO) {
        if (!transaccionRepository.existsById(id)) {
            throw new RuntimeException("Transacción no encontrada con ID: " + id);
        }
        
        // Obtener EstadoTrans
        EstadoTrans estadoTrans = estadoTransRepository.findById(transaccionDTO.getIdEstadoTrans())
                .orElseThrow(() -> new RuntimeException("Estado transacción no encontrado con ID: " + 
                                                      transaccionDTO.getIdEstadoTrans()));
        
        Transaccion transaccion = transaccionMapper.toEntity(transaccionDTO, estadoTrans);
        transaccion.setIdTrans(id);
        Transaccion updatedTransaccion = transaccionRepository.save(transaccion);
        return transaccionMapper.toDto(updatedTransaccion);
    }
    
    // Eliminar transacción
    public void deleteTransaccion(Long id) {
        if (!transaccionRepository.existsById(id)) {
            throw new RuntimeException("Transacción no encontrada con ID: " + id);
        }
        transaccionRepository.deleteById(id);
    }
    
    // hacer una compra
    
    public TransaccionDTO procesarCompra(TransCompraDTO compraDTO) {
        // Variable para controlar si la transacción es exitosa
        boolean transaccionExitosa = true;
        String mensajeError = "Exitoso";

        // Validar moneda
        if (!"USD".equalsIgnoreCase(compraDTO.getMoneda())) {
            transaccionExitosa = false;
            mensajeError = "Solo se permiten compras en USD";
        }

        // Verificar que la tarjeta existe
        Tarjeta tarjeta = null;
        try {
            tarjeta = tarjetaRepository.findById(compraDTO.getCardId())
                    .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + compraDTO.getCardId()));
        } catch (RuntimeException e) {
            transaccionExitosa = false;
            mensajeError = e.getMessage();
        }

        // Solo continuar con las validaciones si la tarjeta existe
        if (tarjeta != null) {
            // Validar estado de la tarjeta
            if (tarjeta.getIdEstado() != 1L) {
                transaccionExitosa = false;
                mensajeError = "La tarjeta debe estar activa para realizar compras";
            }

            // Validar fecha de vencimiento
            try {
                Date fechaVencimiento = tarjeta.getFechaVencimiento();
                Date fechaActual = new Date();
                if (fechaActual.after(fechaVencimiento)) {
                    transaccionExitosa = false;
                    mensajeError = "La tarjeta está vencida";
                }
            } catch (Exception e) {
                transaccionExitosa = false;
                mensajeError = "Error al validar la fecha de vencimiento";
            }

            // Validar saldo suficiente
            if (tarjeta.getSaldo() < compraDTO.getPrice()) {
                transaccionExitosa = false;
                mensajeError = "Saldo insuficiente para realizar la compra";
            }
        }

        // Crear nueva transacción
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setIdTarjeta(compraDTO.getCardId());
        transaccionDTO.setFechaTrans(new Date());
        transaccionDTO.setMoneda(compraDTO.getMoneda());
        transaccionDTO.setIdEstadoTrans(3L); // Estado fallido
        transaccionDTO.setValorTrans(compraDTO.getPrice());
        transaccionDTO.setDescripcion(mensajeError);

        if (transaccionExitosa) {
            transaccionDTO.setIdEstadoTrans(1L); // Estado exitoso
            // Actualizar saldo de la tarjeta
            tarjeta.setSaldo(tarjeta.getSaldo() - compraDTO.getPrice());
            tarjetaRepository.save(tarjeta);
        }

        // Guardar la transacción
        EstadoTrans estadoTrans = estadoTransRepository.findById(transaccionDTO.getIdEstadoTrans())
                .orElseThrow(() -> new RuntimeException("Estado de transacción no encontrado"));
        
        Transaccion savedTransaccion = transaccionRepository.save(transaccionMapper.toEntity(
            transaccionDTO, 
            estadoTrans
        ));
        
        // Crear DTO de respuesta con los campos requeridos
        TransaccionDTO respuestaDTO = new TransaccionDTO();
        respuestaDTO.setIdTrans(savedTransaccion.getIdTrans());
        respuestaDTO.setIdTarjeta(savedTransaccion.getIdTarjeta());
        respuestaDTO.setEstadoTransDescripcion(savedTransaccion.getEstadoTrans().getDescripcion());
        respuestaDTO.setFechaTrans(savedTransaccion.getFechaTrans());
        respuestaDTO.setValorTrans(savedTransaccion.getValorTrans());
        respuestaDTO.setMoneda(savedTransaccion.getMONEDA());
        
        if (!transaccionExitosa) {
            throw new RuntimeException(mensajeError);
        }
        
        return respuestaDTO;
    }
    //anular transaccion
    public RespAnuTransDTO anularTransaccion(TransAnulacionDTO anulacionDTO) {
        // Convertir IDs de String a Long
        Long idTransaccion = Long.parseLong(anulacionDTO.getTransactionId());
        Long idTarjeta = Long.parseLong(anulacionDTO.getCardId());

        // Buscar la transacción original
        Transaccion transaccion = transaccionRepository.findById(idTransaccion)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        // Verificar que la transacción corresponda a la tarjeta
        if (!transaccion.getIdTarjeta().equals(idTarjeta)) {
            throw new RuntimeException("La transacción no corresponde a la tarjeta proporcionada");
        }

        // Verificar que la transacción sea una compra exitosa (estado 1)
        if (transaccion.getEstadoTrans().getIdEstadoTrans() != 1L) {
            throw new RuntimeException("Solo se pueden anular transacciones exitosas");
        }
        
        if (transaccion.getIdanula() !=null ) {
        	throw new RuntimeException("La transacción ya fue anulada bajo el id " + transaccion.getIdanula());
        }

        // Verificar que no hayan pasado más de 24 horas
        Date ahora = new Date();
        long diferenciaHoras = (ahora.getTime() - transaccion.getFechaTrans().getTime()) / (60 * 60 * 1000);
        
        if (diferenciaHoras > 24) {
            throw new RuntimeException("No se pueden anular transacciones con más de 24 horas de antigüedad");
        }

        // Buscar la tarjeta
        Tarjeta tarjeta = tarjetaRepository.findById(idTarjeta)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        // Devolver el saldo a la tarjeta
        tarjeta.setSaldo(tarjeta.getSaldo() + transaccion.getValorTrans());
        tarjetaRepository.save(tarjeta);

        // Crear transacción de anulación
        TransaccionDTO transaccionDTO = new TransaccionDTO();
        transaccionDTO.setIdTarjeta(idTarjeta);
        transaccionDTO.setFechaTrans(new Date());
        transaccionDTO.setValorTrans(transaccion.getValorTrans());
        transaccionDTO.setMoneda(transaccion.getMONEDA());
        transaccionDTO.setIdEstadoTrans(3L); // Estado anulado
        transaccionDTO.setDescripcion("Anulación de transacción: " + idTransaccion);

        // Obtener el estado de anulación y guardar la transacción
        EstadoTrans estadoTrans = estadoTransRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Estado de transacción no encontrado"));
        
        Transaccion savedTransaccion = transaccionRepository.save(transaccionMapper.toEntity(
            transaccionDTO, 
            estadoTrans
        ));
        
        transaccion.setIdanula(savedTransaccion.getIdTrans());
        transaccionRepository.save(transaccion);
        
        // Preparar respuesta
        RespAnuTransDTO respuestaDTO = new RespAnuTransDTO();
        respuestaDTO.setIdTrans(savedTransaccion.getIdTrans());
        respuestaDTO.setIdTarjeta(savedTransaccion.getIdTarjeta());
        respuestaDTO.setFechaTrans(savedTransaccion.getFechaTrans());
        respuestaDTO.setValorTrans(savedTransaccion.getValorTrans());
        respuestaDTO.setDescripcion(savedTransaccion.getDESCRIPCION());
        
        return respuestaDTO;
    }
}
