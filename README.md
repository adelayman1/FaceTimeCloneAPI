![](https://user-images.githubusercontent.com/85571327/215752979-490235d2-7137-43eb-bf7d-6f0559458092.png)
# HttpFaceTimeCloneAPI [![](https://skillicons.dev/icons?i=kotlin&theme=dark)](https://skillicons.dev)

<a href="https://www.linkedin.com/in/adel-ayman/">
    <img src="https://img.shields.io/badge/LinkedIn-blue?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn Badge"/>
  </a>
 
FaceTime API That's Built With Ktor to create  calling rooms..
This repository contains a **FaceTime API** This is an educational API.Use and run this API to learn more about API design and best practices
you must follow.That's built with Ktor , That's implements Coroutines,JWT,Clean Architecture,Koin,mongodb,jackson,etc... using kotlin language with ktor framework.
<br>
You can find postman documentation here -> [Postman Documentation](https://documenter.getpostman.com/view/14561772/2s935isRVa)
<br>
You can find this API in postman here -> [Postman Collection](https://www.postman.com/adelayman1/workspace/facetimecloneapi)

## Table Of Content
- [Endpoints](#endpoints)
  - [User endpoint](#user-endpoint)
    - [Introduction](#introduction)
    - [Operatores](#operators)
      - [Login](#post-login)
      - [Register](#post-register)
      - [FCM Token](#fcm-token)
        - [Edit FCM Token](#patch-edit-fcm-token)
        - [Get FCM Token](#get-get-fcm-token)
      - [Edit Profile](#patch-edit-profile)
      - [User Profile](#get-profile)
      - [Send OTP](#post-send-otp)
      - [Verify OTP](#post-verify-otp)
      - [Delete Account](#del-delete-account)
  - [Rooms endpoint](#rooms-endpoint)
    - [Introduction](#introduction-1)
    - [Operatores](#operators-1)
      - [Create Room](#post-create-room)
      - [Join Room](#post-join-room)
      - [Delete Room](#del-delete-room)
      - [Get Room Info](#get-get-room-info)
      - [Get User Rooms](#get-get-user-rooms)
- [Built With](#built-with-)
- [Project Structure](#project-structure)
- [License](#license)

## Endpoints
## User endpoint
### Introduction
In this part you will find user operators you can make such as
* Login With Email And Password
* Register With Email And Password
* Edit Your Profile
* View Your Profile
* Send Verification Code To Email
* Verify Code sent To Email
* Delete Your Account
### Operators

#### [POST](#endpoints) Login
```
 http://127.0.0.1:8080/user/login
```
**Body** raw (json)
```json
{
  "email": "adelayman0000@gmail.com",
  "password": "123456"
}
```
**Response**
```json
{
    "status": true,
    "data": {
        "userID": "63d5b2c8db392954ecae9737",
        "userToken": "TOKEN",
        "userName": "Adel Ayman",
        "email": "adelayman0000@gmail.com",
        "isVerified": true,
        "fcmToken": "YOUR_FCM_TOKEN"
    },
    "message": "Login done successfully"
}
```
---
#### [POST](#endpoints) Register
```
 http://127.0.0.1:8080/user/register
```
**Body** raw (json)
```json
{
  "name": "Adel Ayman",
  "email": "adelayman0000@gmail.com",
  "password": "123456"
}
```
**Response**
```json
{
    "status": true,
    "data": {
        "userID": "63d8537bf7f5302cce37e4a7",
        "userToken": "TOKEN",
        "userName": "Adel Ayman",
        "email": "test@gmail.com",
        "isVerified": false,
        "fcmToken": "Guest"
    },
    "message": "Registration done successfully"
}
```
---
#### Fcm Token
This part contains **FCM Operators** for example
* Edit FCM Token
* Get User FCM Token By Email

#### [PATCH](#endpoints) Edit FCM Token
In this request you can edit you FCM token. we use your FCM token to send call invitation to you

**Important Note:you must active your account first. you can active account by verify it**

```
http://127.0.0.1:8080/user/fcm-token
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Body** raw (json)
```json
{
    "fcmToken":"YOUR_FCM_TOKEN"
}
```
**Response**
```json
{
    "status": true,
    "data": {
        "userID": "63d5b2c8db392954ecae9737",
        "userToken": null,
        "userName": "Adel Ayman",
        "email": "adelayman0000@gmail.com",
        "isVerified": false,
        "fcmToken": "YOUR_FCM_TOKEN"
    },
    "message": "user details updated successfully"
}
```
---
#### Fcm Token
This part contains **FCM Operators** for example
* Edit FCM Token
* Get User FCM Token By Email

#### [GET](#endpoints) Get FCM Token
In this request you can get your fcm

**Important Note:you must active your account first. you can active account by verify it**

```
http://127.0.0.1:8080/user/fcm-token
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Response**
```json
{
    "status": true,
    "data": "YOUR_FCM_TOKEN",
    "message": "token has got successfully"
}
```
---
#### [PATCH](#endpoints) Edit Profile
In this request you can change your account information like name
**Important Note:you must active your account first. you can active account by verify it**
```
 http://127.0.0.1:8080/user
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Body** raw (json)
```json
{
    "name":"Adel Ayman"
}
```
**Response**
```json
{
    "status": true,
    "data": {
        "userID": "63d5b2c8db392954ecae9737",
        "userToken": null,
        "userName": "Adel Ayman",
        "email": "adelayman0000@gmail.com",
        "isVerified": true,
        "fcmToken": "YOUR_FCM_TOKEN"
    },
    "message": "user details updated successfully"
}
```
---
#### [GET](#endpoints) Profile
In this request you can get your profile and account data

**Important Note:you must active your account first. you can active account by verify it**
```
 http://127.0.0.1:8080/user/profile
```
**Request Headers**

```Authorization```:```Bearer TOKEN```
**Response**
```json
{
    "status": true,
    "data": {
        "userID": "63d5b2c8db392954ecae9737",
        "userToken": null,
        "userName": "Adel Ayman",
        "email": "adelayman0000@gmail.com",
        "isVerified": true,
        "fcmToken": "YOUR_FCM_TOKEN"
    },
    "message": "user profile has got successfully"
}
```
---
#### [POST](#endpoints) Send OTP
Finally you will know how to active your account. In this request you can request send verification code to your email to complete your acccount setup and active it
```
 http://127.0.0.1:8080/user/send-email-code
```
**Request Headers**

```Authorization```:```Bearer TOKEN```
**Response**
```json
{
    "status": true,
    "data": null,
    "message": "otp code has sent successfully"
}
```
---
#### [POST](#endpoints) Verify OTP
In this request you can complete verification of your account by verify the code sent in your email
```
 http://127.0.0.1:8080/user/verify-code
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Query Params**

``` diff
+ otp_code(*required)
```

**Response**
```json
{
    "status": true,
    "data": {
        "userID": "63d5b2c8db392954ecae9737",
        "userToken": "TOKEN",
        "userName": "Adel Ayman",
        "email": "adelayman0000@gmail.com",
        "isVerified": true,
        "fcmToken": "YOUR_FCM_TOKEN"
    },
    "message": "Account has verified successfully"
}
```
---
#### [DEL](#endpoints) Delete Account
Here you can delete your account
```
 http://127.0.0.1:8080/user
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Response**
```json
{
    "status": true,
    "data": null,
    "message": "room has deleted successfully"
}
```

## Rooms endpoint
### Introduction
In this part you will find "calling rooms" operators you can make such as
* Create New Room
* Join To Room
* Delete Created Room
* Get Room Informations
* Get Your Rooms
**Important Note:all requests here required active account. you can active account by verify it**

### Operators

#### [POST](#endpoints) Create Room
In this request you can create new room
There is 3 types of rooms
1:LINK
2:FACETIME
3:AUDIO
```
 http://127.0.0.1:8080/rooms
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Create Link Room Body** raw (json)
```json
{
    "roomType":"LINK"
}
```

**Create FACETIME Room Body** raw (json)
```json
{
    "roomType":"FACETIME",
    "participantsEmails":["adelayman0000@gmail.com"]
}
```

```
roomType = EnumClass[LINK,FACETIME,AUDIO]
```

**Response**
```json
{
    "status": true,
    "data": {
        "roomId": "63d710a884f8db3d80ca4c7d",
        "roomType": {
            "id": 2,
            "type": "FaceTime"
        },
        "roomAuthor": "63d5b2c8db392954ecae9737",
        "participants": [
            {
                "userEmail": "adelayman0000@gmail.com",
                "userName": "Adel Ayman",
                "userId": "63d5b2c8db392954ecae9737",
                "missedCall": true
            }
        ],
        "time": "2023-01-30T02:34:48.542"
    },
    "message": "room created successfully"
}
```
---
#### [POST](#endpoints) Join Room
Join to room you have invited to it (you should be participant).
replace {ROOM_ID} with the id of room
```
 http://127.0.0.1:8080/rooms/{note_id}
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Body** raw (json)
```json
{
    "roomId":"ROOM_ID"
}
```

**Response**
```json
{
    "status": true,
    "data": {
        "roomId": "63d710a884f8db3d80ca4c7d",
        "roomType": {
            "id": 2,
            "type": "FaceTime"
        },
        "roomAuthor": "test",
        "participants": [
            {
                "userEmail": "adelayman0000@gmail.com",
                "userName": "Adel Ayman",
                "userId": "63d5b2c8db392954ecae9737",
                "missedCall": false
            }
        ],
        "time": "2023-01-30T02:34:48.542"
    },
    "message": "user has joined successfully"
}
```
---
#### [DEL](#endpoints) Delete Room
Delete room you have created

replace {ROOM_ID} with the id of room you want to delete
```
 http://127.0.0.1:8080/rooms/{ROOM_ID}
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Response**
```json
{
  "status": true,
  "data": null,
  "message": "room has deleted successfully"
}
```
---
#### [GET](#endpoints) Get Room Info
In this request you will get the info of room

replace {ROOM_ID} with the id of room you want to delete
```
 http://127.0.0.1:8080/rooms/{ROOM_ID}
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Response**
```json
{
    "status": true,
    "data": {
        "roomId": "63d6e8ea04b18a106cf80991",
        "roomType": {
            "id": 1,
            "type": "Link"
        },
        "roomAuthor": "63d5b2c8db392954ecae9737",
        "participants": null,
        "time": "2023-01-29T23:45:14.572"
    },
    "message": "room info has got successfully"
}
```
---
#### [GET](#endpoints) Get User Rooms
Here You can get all rooms you joined to them
```
 http://127.0.0.1:8080/rooms
```
**Request Headers**

```Authorization```:```Bearer TOKEN```

**Response**
```json
{
    "status": true,
    "data": [
        {
            "roomId": "63d70ffd508d7c2a2be07d3a",
            "roomType": {
                "id": 1,
                "type": "Link"
            },
            "roomAuthor": "63d5b2c8db392954ecae9737",
            "participants": null,
            "time": "2023-01-30T02:31:57.618"
        },
        {
            "roomId": "63d710a884f8db3d80ca4c7d",
            "roomType": {
                "id": 2,
                "type": "FaceTime"
            },
            "roomAuthor": "63d5b2c8db392954ecae9737",
            "participants": [
                {
                    "userEmail": "adelayman0000@gmail.com",
                    "userName": "Adel Ayman",
                    "userId": "63d5b2c8db392954ecae9737",
                    "missedCall": true
                }
            ],
            "time": "2023-01-30T02:34:48.542"
        }...........
    ],
    "message": "rooms have got successfully"
}
```

## Built With 🛠

*  [Kotlin](https://kotlinlang.org/) 
*  [Ktor](https://ktor.io/) 
*  [Coroutines](https://kotlinlang.org/docs/coroutines-guide.html)
*  [KMongo](https://litote.org/kmongo/) 
*  Clean architecture
*  [JWT Auth](https://ktor.io/docs/jwt.html) 
*  [Koin](https://insert-koin.io/docs/reference/koin-ktor/ktor) 
*  Repository pattern
*  [jackson](https://github.com/FasterXML/jackson-docs).

## Project Structure
```
📦API
 ┣ 📂data
 ┃ ┣ 📂models
 ┃ ┃ ┣ 📜CallInvitationDataModel.kt
 ┃ ┃ ┣ 📜CallInvitationRequestModel.kt
 ┃ ┃ ┣ 📜Participant.kt
 ┃ ┃ ┣ 📜Room.kt
 ┃ ┃ ┗ 📜User.kt
 ┃ ┣ 📂repositories
 ┃ ┃ ┣ 📜RoomRepositoryImpl.kt
 ┃ ┃ ┗ 📜UserRepositoryImpl.kt
 ┃ ┣ 📂sources
 ┃ ┃ ┣ 📂roomDataSources
 ┃ ┃ ┃ ┣ 📜FcmRemoteDataSource.kt
 ┃ ┃ ┃ ┗ 📜RoomRemoteDataSource.kt
 ┃ ┃ ┗ 📂userDataSources
 ┃ ┃ ┃ ┗ 📜UserRemoteDataSource.kt
 ┃ ┗ 📂utilities
 ┃ ┃ ┣ 📂extensions
 ┃ ┃ ┃ ┣ 📜RoomExtensions.kt
 ┃ ┃ ┃ ┗ 📜UserExtensions.kt
 ┃ ┃ ┗ 📜UserJWTConfig.kt
 ┣ 📂di
 ┃ ┗ 📂modules
 ┃ ┃ ┗ 📜MainModule.kt
 ┣ 📂domain
 ┃ ┣ 📂models
 ┃ ┃ ┣ 📜BaseResponse.kt
 ┃ ┃ ┣ 📜ParticipantModel.kt
 ┃ ┃ ┣ 📜RoomModel.kt
 ┃ ┃ ┣ 📜RoomType.kt
 ┃ ┃ ┗ 📜UserModel.kt
 ┃ ┣ 📂repositories
 ┃ ┃ ┣ 📜RoomRepository.kt
 ┃ ┃ ┗ 📜UserRepository.kt
 ┃ ┗ 📂usecases
 ┃ ┃ ┣ 📜CreateRoomUseCase.kt
 ┃ ┃ ┣ 📜DeleteAccountUseCase.kt
 ┃ ┃ ┣ 📜DeleteRoomUseCase.kt
 ┃ ┃ ┣ 📜GetRoomInfoUseCase.kt
 ┃ ┃ ┣ 📜GetUserFcmTokenUseCase.kt
 ┃ ┃ ┣ 📜GetUserProfileUseCase.kt
 ┃ ┃ ┣ 📜GetUserRoomsUseCase.kt
 ┃ ┃ ┣ 📜JoinRoomUseCase.kt
 ┃ ┃ ┣ 📜LoginUseCase.kt
 ┃ ┃ ┣ 📜RegisterUseCase.kt
 ┃ ┃ ┣ 📜SendCallInvitationUseCase.kt
 ┃ ┃ ┣ 📜SendEmailVerifyCodeUseCase.kt
 ┃ ┃ ┣ 📜UpdateUserDataUseCase.kt
 ┃ ┃ ┗ 📜VerifyCodeUseCase.kt
 ┣ 📂plugins
 ┃ ┣ 📜Monitoring.kt
 ┃ ┣ 📜Routing.kt
 ┃ ┣ 📜Security.kt
 ┃ ┣ 📜Serialization.kt
 ┃ ┗ 📜Sockets.kt
 ┣ 📂routes
 ┃ ┣ 📂rooms
 ┃ ┃ ┣ 📂requestModels
 ┃ ┃ ┃ ┣ 📜CreateRoomParams.kt
 ┃ ┃ ┃ ┗ 📜JoinRoomParams.kt
 ┃ ┃ ┗ 📜roomsRoute.kt
 ┃ ┗ 📂user
 ┃ ┃ ┣ 📂requestsModels
 ┃ ┃ ┃ ┣ 📜CreateUserParams.kt
 ┃ ┃ ┃ ┣ 📜UpdateUserParams.kt
 ┃ ┃ ┃ ┣ 📜UpdateUserTokenParams.kt
 ┃ ┃ ┃ ┗ 📜UserLoginParams.kt
 ┃ ┃ ┗ 📜userRoute.kt
 ┗ 📜Application.kt
```

## LICENSE
```MIT License

Copyright (c) 2022 adelayman1

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.```
