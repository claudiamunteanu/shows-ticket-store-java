package app.network.protobuffprotocol;

import java.io.IOException;

public class TestProto {
    public static void main(String[] args){
        System.out.println("AppRequest: ");
        try{
            ProtoUtils.createLoginRequest("user", "12345").writeTo(System.out);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
