package com.example.axis_inside.tf_exp_app;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.axis_inside.tf_exp_app.Models.SmsModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rishav on 27/6/17.
 */

public class DynamoDBManager {
    private static final String TAG = "DynamoDBManager";
    private static AmazonDynamoDBClient amazonDynamoDBClient;
    private static DynamoDBMapper dynamoDBMapper;

    private static AmazonDynamoDBClient getAmazonDynamoDBClient(Context context){
        if(amazonDynamoDBClient == null){
            return new AmazonClientManager(context).ddb();
        }
        return amazonDynamoDBClient;
    }

    private static DynamoDBMapper getDynamoDBMapper(Context context){
        if(dynamoDBMapper == null){
            return new DynamoDBMapper(getAmazonDynamoDBClient(context));
        }
        return dynamoDBMapper;
    }
    public static void insertUsers(Context context, String data) {
        final DynamoDBMapper mapper = getDynamoDBMapper(context);
        final UserLocation userLocation = new UserLocation();
        int tmp = (new Random().nextInt(5));
        userLocation.setUserId(String.valueOf(System.currentTimeMillis()));
        userLocation.setData(data);
        userLocation.setTimeStamp(System.currentTimeMillis());
        new Thread(new Runnable() {
            @Override
            public void run() {
                mapper.save(userLocation);
            }
        }).start();


    }

    public static void insertSms(Context context, final ArrayList<SmsModel> smsModel){
        final DynamoDBMapper mapper = getDynamoDBMapper(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mapper.batchSave(smsModel);
            }
        }).start();
    }

    @DynamoDBTable(tableName = Constants.TEST_TABLE_NAME)
    public static class UserLocation {
        private String userid;

        public double getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(double timeStamp) {
            this.timeStamp = timeStamp;
        }

        private double timeStamp;

        @DynamoDBHashKey(attributeName = "userid")
        public String getUserid() {
            return userid;
        }

        public void setUserId(String userid) {
            this.userid = userid;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        private String data;



    }
}
