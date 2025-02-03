package com.componentes.Tarjetas.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.componentes.Tarjetas.Entity.Tarjeta;
import com.componentes.Tarjetas.Repository.ProductoRepository;
import com.componentes.Tarjetas.Repository.TarjetaRepository;
import com.componentes.Tarjetas.dtos.SaldoTarjDTO;
import com.componentes.Tarjetas.dtos.TarjetaDTO;
import com.componentes.Tarjetas.mappers.TarjetaMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private TarjetaMapper tarjetaMapper;
    
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("MM/yyyy");

    public List<TarjetaDTO> getAllTarjetas() {
        return tarjetaRepository.findAll()
                .stream()
                .map(tarjetaMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<TarjetaDTO> getTarjetaById(Long id) {
        return tarjetaRepository.findById(id)
                .map(tarjetaMapper::toDto);
    }

    // Método principal para guardar tarjeta
    public TarjetaDTO saveTarjeta(TarjetaDTO tarjetaDTO) {
    	
        // Validar que exista el producto
        if (tarjetaDTO.getIdProducto() == null) {
            throw new RuntimeException("Debe especificar el ID del producto");
        }
        
        if (tarjetaDTO.getTitular() == null || tarjetaDTO.getTitular().trim().isEmpty()) {
            throw new RuntimeException("Debe especificar el titular de la tarjeta");
        }
        
        if (!productoRepository.existsById(tarjetaDTO.getIdProducto())) {
            throw new RuntimeException("No existe un producto con el ID: " + tarjetaDTO.getIdProducto());
        }

        // Generar número completo de tarjeta
        Long numeroTarjetaCompleto = generateCardNumber(tarjetaDTO.getIdProducto());
        
        // Establecer valores por defecto y calculados
        tarjetaDTO.setCardId(numeroTarjetaCompleto);
        tarjetaDTO.setIdEstado(2L);  // Estado por defecto
        tarjetaDTO.setSaldo(0.0);    // Saldo inicial
        tarjetaDTO.setMoneda("USD");;
        // Establecer fechas
        Date fechaActual = new Date();
        tarjetaDTO.setFechaCreacion(formatoFecha.format(fechaActual));
        
        // Calcular fecha de vencimiento (3 años después)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);
        calendar.add(Calendar.YEAR, 3);
        tarjetaDTO.setFechaVencimiento(formatoFecha.format(calendar.getTime()));
        
        // Asignar el número de tarjeta generado
        tarjetaDTO.setCardId(numeroTarjetaCompleto);
        
        // Guardar la tarjeta
        Tarjeta tarjeta = tarjetaMapper.toEntity(tarjetaDTO);
        Tarjeta savedTarjeta = tarjetaRepository.save(tarjeta);
        return tarjetaMapper.toDto(savedTarjeta);
    }
    
    
    // crear el numero de tarjeta 
    
    public TarjetaDTO generateTarjeta(Long idProducto) {
        // Validar que exista el producto
        if (!productoRepository.existsById(idProducto)) {
            throw new RuntimeException("No existe un producto con el ID: " + idProducto);
        }
        
        // Crear nuevo DTO con valores por defecto
        TarjetaDTO tarjetaDTO = new TarjetaDTO();
        
        // Establecer el ID de producto
        tarjetaDTO.setIdProducto(idProducto);
        
        // Generar número de tarjeta
        Long numeroTarjetaCompleto = generateCardNumber(idProducto);
        tarjetaDTO.setCardId(numeroTarjetaCompleto);
        
        // Establecer valores por defecto
        tarjetaDTO.setIdEstado(2L);  // Estado por defecto (inactivo)
        tarjetaDTO.setTitular("sin cliente asignado");
        tarjetaDTO.setSaldo(0.0);
        tarjetaDTO.setMoneda("USD");;
        
        // Establecer fechas
        Date fechaActual = new Date();
        tarjetaDTO.setFechaCreacion(formatoFecha.format(fechaActual));
        
        // Calcular fecha de vencimiento (3 años después)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);
        calendar.add(Calendar.YEAR, 3);
        tarjetaDTO.setFechaVencimiento(formatoFecha.format(calendar.getTime()));
        
        // Guardar la tarjeta
        Tarjeta tarjeta = tarjetaMapper.toEntity(tarjetaDTO);
        Tarjeta savedTarjeta = tarjetaRepository.save(tarjeta);
        return tarjetaMapper.toDto(savedTarjeta);
    }

    private Long generateCardNumber(Long idProducto) {
        // Validar que el ID del producto tenga 6 dígitos
        String idProductoStr = String.format("%06d", idProducto);
        if (idProductoStr.length() != 6) {
            throw new RuntimeException("El ID del producto debe tener 6 dígitos");
        }

        // Generar 10 dígitos aleatorios
        Random random = new Random();
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            randomPart.append(random.nextInt(10));
        }

        // Combinar ID del producto con números aleatorios
        String cardNumber = idProductoStr + randomPart.toString();
        
        // Verificar que el número generado no exista ya
        Long numeroTarjeta = Long.parseLong(cardNumber);
        int intentos = 0;
        while (tarjetaRepository.existsById(numeroTarjeta) && intentos < 5) {
            // Si existe, generar otro número
            randomPart = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                randomPart.append(random.nextInt(10));
            }
            cardNumber = idProductoStr + randomPart.toString();
            numeroTarjeta = Long.parseLong(cardNumber);
            intentos++;
        }
        
        if (intentos >= 5) {
            throw new RuntimeException("No se pudo generar un número de tarjeta único después de 5 intentos");
        }

        return numeroTarjeta;
    }

    public TarjetaDTO updateTarjeta(Long id, TarjetaDTO tarjetaDTO) {
        // Verificar que la tarjeta existe
        Tarjeta tarjetaExistente = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + id));
        
        // Validar que solo se estén modificando los campos permitidos
        if (tarjetaDTO.getTitular() != null && !tarjetaDTO.getTitular().equals(tarjetaExistente.getTitular())) {
            throw new RuntimeException("No se permite modificar el titular de la tarjeta");
        }
        
        if (tarjetaDTO.getIdProducto() != null && !tarjetaDTO.getIdProducto().equals(tarjetaExistente.getIdProducto())) {
            throw new RuntimeException("No se permite modificar el producto de la tarjeta");
        }
        
        // Mantener los valores que no deben cambiar
        tarjetaDTO.setCardId(id);
        tarjetaDTO.setIdProducto(tarjetaExistente.getIdProducto());
        tarjetaDTO.setTitular(tarjetaExistente.getTitular());
        tarjetaDTO.setFechaCreacion(formatoFecha.format(tarjetaExistente.getFechaCreacion()));
        tarjetaDTO.setFechaVencimiento(formatoFecha.format(tarjetaExistente.getFechaVencimiento()));
        
        // Solo permitir actualizar saldo y estado
        if (tarjetaDTO.getSaldo() != null) {
            tarjetaExistente.setSaldo(tarjetaDTO.getSaldo());
        }
        
        if (tarjetaDTO.getIdEstado() != null) {
            tarjetaExistente.setIdEstado(tarjetaDTO.getIdEstado());
        }
        tarjetaDTO.setMoneda(tarjetaExistente.getMONEDA());
        // Guardar los cambios
        Tarjeta updatedTarjeta = tarjetaRepository.save(tarjetaExistente);
        return tarjetaMapper.toDto(updatedTarjeta);
    }
    //Activar la tarjeta 
    public TarjetaDTO activarTarjeta(Long id) {
        // Verificar que la tarjeta existe
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + id));
        
        // Si ya está activa, lanzar excepción
        if (tarjeta.getIdEstado() == 1L) {
            throw new RuntimeException("La tarjeta ya se encuentra activa");
        }
        
        // Cambiar a estado activo
        tarjeta.setIdEstado(1L);
        
        // Guardar los cambios
        Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);
        return tarjetaMapper.toDto(updatedTarjeta);
    }
    //desactivar la tarjeta 
    public TarjetaDTO desactivarTarjeta(Long id) {
        // Verificar que la tarjeta existe
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + id));
        
        // Si ya está inactiva, lanzar excepción
        if (tarjeta.getIdEstado() == 2L) {
            throw new RuntimeException("La tarjeta ya se encuentra inactiva");
        }
        
        // Cambiar a estado inactivo
        tarjeta.setIdEstado(2L);
        
        // Guardar los cambios
        Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);
        return tarjetaMapper.toDto(updatedTarjeta);
    }

    public void deleteTarjeta(Long id) {
        tarjetaRepository.deleteById(id);
    }
    
    //bloquear una  tarjeta 
    public TarjetaDTO bloquearTarjeta(Long id) {
        // Verificar que la tarjeta existe
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + id));
        
        // Si ya está bloqueada (estado 2), lanzar excepción
        if (tarjeta.getIdEstado() == 3L) {
            throw new RuntimeException("La tarjeta ya se encuentra bloqueada");
        }
        
        // Cambiar a estado bloqueado (2)
        tarjeta.setIdEstado(3L);
        
        // Guardar los cambios
        Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);
        return tarjetaMapper.toDto(updatedTarjeta);
    }
    
    //recargar saldo 
    public TarjetaDTO recargarTarjeta(Long cardId, Double montoRecarga) {
        // Verificar que la tarjeta existe
        Tarjeta tarjeta = tarjetaRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + cardId));
        
        // Validar el estado de la tarjeta
        if (tarjeta.getIdEstado() == 2L) {
            throw new RuntimeException("La tarjeta está inactiva. Debe activar la tarjeta antes de realizar una recarga");
        }
        
        if (tarjeta.getIdEstado() == 3L) {
            throw new RuntimeException("La tarjeta está bloqueada. Debe desbloquear la tarjeta antes de realizar una recarga");
        }
        
        // Validar que el estado sea activo (1)
        if (tarjeta.getIdEstado() != 1L) {
            throw new RuntimeException("La tarjeta debe estar activa para realizar recargas");
        }
                
        // Validar que el monto de recarga sea positivo
        if (montoRecarga <= 0) {
            throw new RuntimeException("El monto de recarga debe ser mayor a 0");
        }
        
        // Sumar el nuevo saldo
        Double saldoActual = tarjeta.getSaldo();
        tarjeta.setSaldo(saldoActual + montoRecarga);
        
        // Guardar los cambios
        Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);
        return tarjetaMapper.toDto(updatedTarjeta);
    }
    
    //saldo de la tarjeta 
    public SaldoTarjDTO consultarBalance(Long cardId) {
        // Verificar que la tarjeta existe
        Tarjeta tarjeta = tarjetaRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + cardId));
        
        // Crear DTO con la información solicitada
        SaldoTarjDTO saldoInfo = new SaldoTarjDTO();
        saldoInfo.setCardId(cardId);
        saldoInfo.setBalance(tarjeta.getSaldo());
        
        return saldoInfo;
    }
    
    //asignarle un titular a la tarjeta
    public TarjetaDTO asignarTitular(Long cardId, String titular) {
        // Verificar que la tarjeta existe
        Tarjeta tarjeta = tarjetaRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("No existe una tarjeta con el ID: " + cardId));
                
        // Validar que el titular no sea nulo o vacío
        if (titular == null || titular.trim().isEmpty()) {
            throw new RuntimeException("El titular no puede estar vacío");
        }
        
        // Validar que la tarjeta no tenga titular asignado
        if (!"sin cliente asignado".equals(tarjeta.getTitular())) {
            throw new RuntimeException("La tarjeta ya tiene un titular asignado");
        }
        
        // Asignar el nuevo titular
        tarjeta.setTitular(titular);
        
        // Guardar los cambios
        Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);
        return tarjetaMapper.toDto(updatedTarjeta);
    }
}