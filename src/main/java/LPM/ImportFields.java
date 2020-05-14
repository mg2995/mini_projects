package LPM;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

public class ImportFields {
    public static void main(String[] args) {
        try {
            System.out.println("inside");
            getAllFields();
        } catch (IOException e) {
            System.out.println("exception found");
        }
    }

    private static void getAllFields() throws IOException {
        /**
         * Change the url to fields & the object to FieldResponse
         */
        String url = "http://organic-solr-frontend.us-east-1.ec2.bloomreach.com/solr/neimanmarcus_lpm_pagedata_v2_shard1_replica1/schema/dynamicfields";
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(1, false));
        try {
            System.out.println("yo");
            int statusCode = client.executeMethod(method);
            System.out.println("api called");
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            byte[] responseBody = method.getResponseBody();
            System.out.println(new String(responseBody));
            String response = new String(responseBody);
            ObjectMapper mapper = new ObjectMapper();
            DynamicFieldResponse result = mapper.readValue(response, DynamicFieldResponse.class);
            int count = 0;
            for (int ind = 0; ind < result.dynamicFields.size(); ind++) {
                DynamicFieldResponse.Field field = result.dynamicFields.get(ind);
                System.out.print(field.name + "\t" + field.type + "\t" + field.indexed + "\t" + field.stored);
                if (field.required != null) {
                    System.out.print("     required   \t " + field.required);
                }
                if (field.multiValued != null) {
                    System.out.print("    multivalued   \t " + field.multiValued);
                }
                count++;
                System.out.println();
            }
            System.out.println(count);
        } catch (Exception e) {
            System.out.println("something is wrong");
        }

    }
}
