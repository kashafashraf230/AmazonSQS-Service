package service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import java.util.List;


public class SQSService {

    public static void main(String[] args){

        publishMsg();
        System.out.println("Msg Published");
        retrieveMgs();
        System.out.println("Msg Retrieved");

    }

    public static void publishMsg(){
        String queueURL = "http://localhost:4566/000000000000/MyQueue";
        String messageBody = "Hello, SQS!";

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:4566","us-east-2"))
                .build();

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueURL)
                .withMessageBody(messageBody);

        sqs.sendMessage(sendMessageRequest);
        System.out.println("Message sent to SQS");
    }

    public static void retrieveMgs() {
        String queueURL = "http://localhost:4566/000000000000/MyQueue";
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:4566","us-east-2"))
                .build();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueURL)
                .withMaxNumberOfMessages(1);

        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        for (Message message : messages) {
            System.out.println("Received message: " + message.getBody());

            // Delete the message from the queue after processing
            sqs.deleteMessage(queueURL, message.getReceiptHandle());
        }
    }
}
