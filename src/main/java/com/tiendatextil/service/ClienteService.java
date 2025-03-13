package com.tiendatextil.service;

import com.tiendatextil.model.Cliente;
import com.tiendatextil.dto.ClienteDTO;
import com.tiendatextil.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Crear un nuevo cliente
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertToDTO(clienteGuardado);
    }

    // Obtener todos los clientes
    public List<ClienteDTO> obtenerClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener un cliente por su ID
    public Optional<ClienteDTO> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Obtener un cliente por nombre
    public Optional<ClienteDTO> obtenerClientePorNombre(String nombre) {
        return clienteRepository.findByNombre(nombre)
                .map(this::convertToDTO);
    }

    // Actualizar un cliente
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        if (clienteRepository.existsById(id)) {
            Cliente cliente = new Cliente();
            cliente.setId(id);
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setEmail(clienteDTO.getEmail());
            cliente.setTelefono(clienteDTO.getTelefono());
            
            Cliente clienteActualizado = clienteRepository.save(cliente);
            return convertToDTO(clienteActualizado);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // Eliminar un cliente
    public void eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // Método privado para convertir de Entity a DTO
    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        return dto;
    }
}
