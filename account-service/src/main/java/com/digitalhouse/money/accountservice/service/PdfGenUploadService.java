package com.digitalhouse.money.accountservice.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.lowagie.text.DocumentException;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public interface PdfGenUploadService {
    void generatePDFFile(String templateName, Context context, UUID accountNumber, UUID transactionId) throws DocumentException;

    void uploadPdfToS3(ByteArrayOutputStream data, String accountNumber, String transactionId);

//    URL getPdfS3Url(UUID accountId, UUID transactionId);

    S3ObjectInputStream getS3Object(UUID accountId, UUID transactionId);
}
