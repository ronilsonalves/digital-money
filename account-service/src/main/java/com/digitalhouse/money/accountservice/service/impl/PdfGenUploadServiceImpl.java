package com.digitalhouse.money.accountservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import com.digitalhouse.money.accountservice.data.repository.TransactionRepository;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.service.PdfGenUploadService;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfGenUploadServiceImpl implements PdfGenUploadService {

    private final SpringTemplateEngine templateEngine;
    private final TransactionRepository transactionRepository;

    private final AmazonS3 s3Client;

    @Override
    public void generatePDFFile(String templateName, Context context, UUID accountNumber, UUID transactionId) throws DocumentException {
        String htmlContent = templateEngine.process(templateName,context);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream,false);
        renderer.finishPDF();
        uploadPdfToS3(outputStream,accountNumber.toString(),transactionId.toString());
    }

    public void uploadPdfToS3(ByteArrayOutputStream data, String accountNumber, String transactionId) {
        InputStream inputStream = new ByteArrayInputStream(data.toByteArray());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.size());
        s3Client.putObject("piback-g2",accountNumber+"/"+transactionId+".pdf",inputStream,metadata);
    }

//    @Override
//    public URL getPdfS3Url(UUID accountId, UUID transactionId) {
//        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()->new ResourceNotFoundException(
//                "Transaction not found."));
//        if (!(transaction.getOriginAccountNumber().equals(accountId) || transaction.getRecipientAccountNumber().equals(accountId))) {
//            throw new UnauthorizedException("User not authorized to perform this action.");
//        }
//        String keyName = accountId+"/"+transactionId+".pdf";
//        Date expiration = new Date(System.currentTimeMillis() + 3600000);
//        GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest("piback-g2",keyName, HttpMethod.GET)
//                .withExpiration(expiration);
//        return s3Client.generatePresignedUrl(presignedUrlRequest);
//    }

    @Override
    public S3ObjectInputStream getS3Object(UUID accountId, UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()->new ResourceNotFoundException(
                "Transaction not found."));
        if (!(transaction.getOriginAccountNumber().equals(accountId) || transaction.getRecipientAccountNumber().equals(accountId))) {
            throw new UnauthorizedException("User not authorized to perform this action.");
        }
        String keyName = accountId+"/"+transactionId+".pdf";
        return s3Client.getObject("piback-g2",keyName).getObjectContent();
    }
}
