package com.example.demo.service;

import com.example.demo.model.Pet;
import com.example.demo.model.Client;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public PetService(PetRepository petRepository, ClientRepository clientRepository) {
        this.petRepository = petRepository;
        this.clientRepository = clientRepository;
    }

    public Pet createPet(Pet pet, Integer clientId) {
        if (petRepository.existsByClientIdAndName(clientId, pet.getName())) {
            throw new ValidationException("A pet with this name already exists for this client");
        }
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ValidationException("Client not found with id: " + clientId));
        pet.setClient(client);
        return petRepository.save(pet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByClientId(Integer clientId) {
        return petRepository.findByClientId(clientId);
    }

    public Optional<Pet> getPetById(Integer id) {
        return petRepository.findById(id);
    }

    public Pet updatePet(Integer id, Pet petDetails) {
        return petRepository.findById(id)
            .map(existingPet -> {
                existingPet.setName(petDetails.getName());
                existingPet.setSpecies(petDetails.getSpecies());
                existingPet.setBreed(petDetails.getBreed());
                existingPet.setBirthDate(petDetails.getBirthDate());
                return petRepository.save(existingPet);
            })
            .orElseThrow(() -> new ValidationException("Pet not found with id: " + id));
    }

    public void deletePet(Integer id) {
        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Pet not found with id: " + id));
        petRepository.delete(pet);
    }
}