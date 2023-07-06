import java.util.*;
import java.util.stream.*;
import com.africastalking.*;

public class ApartmentBookingApp {
    // Replace with your Africastalking credentials
    private static final String USERNAME = "Miriam Wajohi";
    private static final String API_KEY = "faf53c3255777aa68fc6a2d3e7edc3a1f1ac2dc9be72bc20a98ac33dc0b1a6f8";

    public static void main(String[] args) {
        // Initialize Africastalking API
        AfricasTalking.initialize(USERNAME, API_KEY);
        final SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

        // Handle USSD POST request
        // Read the variables sent via POST from our API
        Map<String, String> body = Arrays
            .asList(args)
            .stream()
            .map(entry -> entry.split("="))
            .collect(Collectors.toMap(entry -> entry[0], entry -> entry.length == 2 ? entry[1] : ""));

        String sessionId = body.get("sessionId");
        String serviceCode = body.get("serviceCode");
        String phoneNumber = body.get("phoneNumber");
        String text = body.get("text");

        StringBuilder response = new StringBuilder("");

        if (text.isEmpty()) {
            // This is the first request. Note how we start the response with CON
            response.append("CON Welcome to the Apartment Booking App\n");
            response.append("Please enter the number of bedrooms you want to book:");

        } else {
            // Process the user's input
            int bedrooms = Integer.parseInt(text);

            // Perform booking logic based on the number of bedrooms
            if (bedrooms > 0) {
                // Booking successful
                response.append("END Thank you for booking ").append(bedrooms).append(" bedroom(s).\n");
                response.append("Confirmation message will be sent to ").append(phoneNumber);

                // Send a confirmation SMS to the user
                String message = "Thank you for booking " + bedrooms + " bedroom(s).";
                String[] recipients = { phoneNumber };
                sms.send(message, "ApartmentApp", recipients);
            } else {
                // Invalid input, ask the user to enter a valid number of bedrooms
                response.append("CON Invalid input. Please enter the number of bedrooms you want to book:");
            }
        }

        // Print the response
        System.out.println(response.toString());
    }
}

