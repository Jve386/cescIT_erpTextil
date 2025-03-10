package com.tiendatextil.service;

import com.tiendatextil.dto.ClienteDTO;
import com.tiendatextil.model.Cliente;
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
        Cliente cliente = convertirACliente(clienteDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirAClienteDTO(clienteGuardado);
    }

    // Obtener todos los clientes
    public List<ClienteDTO> obtenerClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                       .map(this::convertirAClienteDTO)
                       .collect(Collectors.toList());
    }

    // Obtener un cliente por su ID
    public Optional<ClienteDTO> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).map(this::convertirAClienteDTO);
    }

    // Actualizar un cliente
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    clienteExistente.setNombre(clienteDTO.getNombre());
                    clienteExistente.setEmail(clienteDTO.getEmail());
                    clienteExistente.setTelefono(clienteDTO.getTelefono());
                    Cliente clienteActualizado = clienteRepository.save(clienteExistente);
                    return convertirAClienteDTO(clienteActualizado);
                })
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    // Eliminar un cliente
    public void eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // Métodos de conversión entre DTO y entidad
    private ClienteDTO convertirAClienteDTO(Cliente cliente) {
        return new ClienteDTO(cliente.getId(), cliente.getNombre(), cliente.getEmail(), cliente.getTelefono());
    }

    private Cliente convertirACliente(ClienteDTO clienteDTO) {
        return new Cliente(clienteDTO.getNombre(), clienteDTO.getEmail(), clienteDTO.getTelefono());
    }
}
