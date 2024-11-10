package com.example.SpringAppGB.Authorization.interfaces;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Интерфейс для работы с ключами шифрования, включая получение, генерацию и запись ключей.
 * Реализующие классы должны обеспечивать доступ к приватным и публичным ключам, а также возможность
 * генерации пары ключей и их сохранения в файлы.
 */
public interface KeyProvider {

    /**
     * Получает приватный ключ.
     *
     * @return Приватный ключ
     * @throws NoSuchAlgorithmException если алгоритм для генерации ключа не найден
     * @throws InvalidKeySpecException  если спецификация ключа недействительна
     * @throws IOException              если ошибка ввода/вывода
     */
    PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

    /**
     * Получает публичный ключ.
     *
     * @return Публичный ключ
     * @throws NoSuchAlgorithmException если алгоритм для генерации ключа не найден
     * @throws InvalidKeySpecException  если спецификация ключа недействительна
     * @throws IOException              если ошибка ввода/вывода
     */
    PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

    /**
     * Генерирует пару ключей (приватный и публичный).
     *
     * @return Пара ключей (приватный и публичный)
     * @throws NoSuchAlgorithmException если алгоритм для генерации ключей не найден
     */
    KeyPair generateKeyPair() throws NoSuchAlgorithmException;

    /**
     * Сохраняет приватный ключ в файл.
     *
     * @param privateKey Приватный ключ для сохранения
     * @throws IOException если ошибка записи в файл
     */
    void savePrivateKeyToFile(RSAPrivateKey privateKey) throws IOException;  // Запись приватного ключа

    /**
     * Сохраняет публичный ключ в файл.
     *
     * @param publicKey Публичный ключ для сохранения
     * @throws IOException если ошибка записи в файл
     */
    void savePublicKeyToFile(RSAPublicKey publicKey) throws IOException;     // Запись публичного ключа
}
