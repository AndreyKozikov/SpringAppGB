package com.example.SpringAppGB.Authorization.services;

import com.example.SpringAppGB.Authorization.interfaces.KeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Реализация интерфейса KeyProvider для работы с RSA-ключами для JWT.
 * Этот сервис позволяет генерировать, сохранять и загружать RSA-ключи из файлов для использования в JWT.
 */
@Service
public class JwtKeyProvider implements KeyProvider {

    @Value("${jwt.private_key_path}")
    private String privateKeyPath;

    @Value("${jwt.public_key_path}")
    private String publicKeyPath;

    /**
     * Загрузка приватного ключа из файла.
     * Преобразует Base64-кодированное содержимое файла в объект PrivateKey.
     * Если файл не существует, генерирует новую пару ключей.
     *
     * @return объект RSAPrivateKey, загруженный из файла
     * @throws NoSuchAlgorithmException если алгоритм RSA не поддерживается
     * @throws InvalidKeySpecException если спецификация ключа некорректна
     * @throws IOException если файл не найден или недоступен
     */
    @Override
    public RSAPrivateKey getPrivateKey() throws
            NoSuchAlgorithmException,
            InvalidKeySpecException,
            IOException {
        if (!Files.exists(Path.of(privateKeyPath))) {
            // Если файла нет, вызываем метод для его создания
            generateKeyPair();
        }
        String keyContent = new String(Files.readAllBytes(Paths.get(privateKeyPath)))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");  // Удаляем пробелы и переносы строк

        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * Загрузка публичного ключа из файла.
     * Преобразует Base64-кодированное содержимое файла в объект PublicKey.
     * Если файл не существует, генерирует новую пару ключей.
     *
     * @return объект RSAPublicKey, загруженный из файла
     * @throws NoSuchAlgorithmException если алгоритм RSA не поддерживается
     * @throws InvalidKeySpecException если спецификация ключа некорректна
     * @throws IOException если файл не найден или недоступен
     */
    @Override
    public RSAPublicKey getPublicKey() throws
            NoSuchAlgorithmException,
            InvalidKeySpecException,
            IOException {
        if (!Files.exists(Path.of(publicKeyPath))) {
            // Если файла нет, вызываем метод для его создания
            generateKeyPair();
        }
        String keyContent = new String(Files.readAllBytes(Paths.get(publicKeyPath)))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");  // Удаляем пробелы и переносы строк

        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }


    /**
     * Сохранение приватного ключа в файл в формате PEM.
     * Генерирует строку в формате PEM и сохраняет ее в указанный путь.
     *
     * @param privateKey приватный ключ для сохранения
     * @throws IOException если возникает ошибка при записи в файл
     */
    @Override
    public void savePrivateKeyToFile(RSAPrivateKey privateKey) throws IOException {
        createDirectoriesIfNotExists(Path.of(privateKeyPath));
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN PRIVATE KEY-----\n");
        sb.append(java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        sb.append("\n-----END PRIVATE KEY-----\n");
        // Проверка, существует ли сам файл
        if (!Files.exists(Path.of(privateKeyPath))) {
            Files.createFile(Path.of(privateKeyPath));  // Создаем файл, если его нет
        }
        Files.write(Paths.get(privateKeyPath), sb.toString().getBytes());
    }

    /**
     * Сохранение публичного ключа в файл в формате PEM.
     * Генерирует строку в формате PEM и сохраняет ее в указанный путь.
     *
     * @param publicKey публичный ключ для сохранения
     * @throws IOException если возникает ошибка при записи в файл
     */
    @Override
    public void savePublicKeyToFile(RSAPublicKey publicKey) throws IOException {
        createDirectoriesIfNotExists(Path.of(publicKeyPath));
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN PUBLIC KEY-----\n");
        sb.append(java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        sb.append("\n-----END PUBLIC KEY-----\n");
        if (!Files.exists(Path.of(publicKeyPath))) {
            Files.createFile(Path.of(publicKeyPath));  // Создаем файл, если его нет
        }
        Files.write(Paths.get(publicKeyPath), sb.toString().getBytes());
    }

    /**
     * Создание директорий, если они не существуют.
     * Используется для создания пути к файлам ключей.
     *
     * @param path путь, для которого необходимо создать директории
     * @throws IOException если возникает ошибка при создании директорий
     */
    private void createDirectoriesIfNotExists(Path path) throws IOException {
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
    }

    /**
     * Генерация новой пары RSA-ключей (приватный и публичный).
     * Создает пару ключей с длиной 2048 бит и сохраняет их в файлы.
     *
     * @return сгенерированная пара ключей RSA
     * @throws NoSuchAlgorithmException если алгоритм RSA не поддерживается
     */
    @Override
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); // Генерация пары ключей
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        try {
            savePublicKeyToFile(publicKey);
            savePrivateKeyToFile(privateKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return keyPairGenerator.generateKeyPair();
    }
}
