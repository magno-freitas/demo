package com.example.demo.service;

import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import com.example.demo.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new ValidationException("Email already registered: " + client.getEmail());
        }
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Integer id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(Integer id, Client clientDetails) {
        return clientRepository.findById(id)
            .map(existingClient -> {
                existingClient.setName(clientDetails.getName());
                existingClient.setEmail(clientDetails.getEmail());
                existingClient.setPhone(clientDetails.getPhone());
                existingClient.setAddress(clientDetails.getAddress());
                return clientRepository.save(existingClient);
            })
            .orElseThrow(() -> new ValidationException("Client not found with id: " + id));
    }

    public void deleteClient(Integer id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Client not found with id: " + id));
        clientRepository.delete(client);
    }
}