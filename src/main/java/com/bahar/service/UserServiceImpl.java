package com.bahar.service;

import com.shopping.stubs.user.Gender;
import com.shopping.stubs.user.UserRequest;
import com.shopping.stubs.user.UserResponse;
import com.shopping.stubs.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import shopping.User;
import shopping.UserDao;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
      //  super.getUserDetails(request, responseObserver);
        UserDao userDao =new UserDao();
        User user= userDao.getDetails(request.getUsername());

        //make a builder in order to convert user info to userResponse info
        UserResponse.Builder  userResponseBuilder =
                UserResponse.newBuilder()
                        .setId(user.getId())
                        .setUsername(user.getName())
                        .setGender(Gender.valueOf(user.getGender()))
                        .setName(user.getName());

        //then build userResponse object
        UserResponse userResponse = userResponseBuilder.build();

        //get observer and call next method for what??? To return userResponse to client.
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }
}
