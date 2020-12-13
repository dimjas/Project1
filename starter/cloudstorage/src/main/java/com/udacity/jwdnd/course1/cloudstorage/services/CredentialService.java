package com.udacity.jwdnd.course1.cloudstorage.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int insert(Credential credential) {
        String key = createKey();

        return credentialMapper.insert(new Credential(null, credential.getUrl(), credential.getUsername(),
                encryptionService.encryptValue(credential.getPassword(), key), key, credential.getUserId()));
    }

    public int update(Credential credentialForm) {
        Credential credential = getCredential(credentialForm.getCredentialId());
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        return credentialMapper.update(credential);
    }

    public void delete(int credentialId) {
        credentialMapper.delete(credentialId);
    }

    public Credential getCredential(int credentialId) {
        Credential credential = credentialMapper.getCredential(credentialId);
        if (credential != null) {
            String plainTextPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setPassword(plainTextPassword);
        }
        return credential;
    }

    public List<Credential> getCredentialsForUser(int userId) {
        return credentialMapper.getCredentialsForUser(userId);
    }

    private String createKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
