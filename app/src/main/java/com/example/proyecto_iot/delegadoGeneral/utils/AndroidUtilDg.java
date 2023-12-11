package com.example.proyecto_iot.delegadoGeneral.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.alumno.Entities.Alumno;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AndroidUtilDg {
    public static void setPerfilImg (Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public static void enviarCorreo(Alumno alumno, String estatus) {
        try {
            String emailFrom = "activiconnect@gmail.com";
            String passwFrom = "tbxq bxhk cydd vwnv";
            String correoAlumno = alumno.getCorreo();

            String host = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host",host);
            properties.put("mail.smtp.port","465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth","true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailFrom, passwFrom);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoAlumno));
            mimeMessage.setSubject("Cuenta ActiviConnect");

            if(estatus.equals("baneado")){
                mimeMessage.setText("Su cuenta ha sido baneada \n\nPara más información, contáctese con el delegado del general.\n\nActiviConnect");
            }
            if(estatus.equals("desbaneado")){
                mimeMessage.setText("Su cuenta ha sido desbaneada \n\nPara más información, contáctese con el delegado del general.\n\n¡Que tus días de Semana sean los más entretenidos! :D\nActiviConnect");
            }
            if (estatus.equals("rechazado")){
                mimeMessage.setText("Su solicitud de registro ha sido denegada \n\nPara más información, contáctese con el delegado del general.\n\nActiviConnect");
            }
            if (estatus.equals("aceptado")){
                mimeMessage.setText("Su solicitud de registro ha sido aprobada \n\nPara más información, contáctese con el delegado del general.\n\n¡Que tus días de Semana sean los más entretenidos! :D\nActiviConnect");
            }

            Thread thead = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        Log.d("msg-test","se envió correo a "+correoAlumno);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thead.start();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
