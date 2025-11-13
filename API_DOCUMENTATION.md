# TaskFlow API Documentation

## Authentication

All APIs (except `/api/auth/login` and `/api/auth/register`) require authentication via JWT Token.

**Required Header:**
```
Authorization: Bearer <jwt_token>
```

---

## Auth API

### 1. Register User

**Endpoint:** `POST /api/auth/register`

**Authentication:** Not required

**Description:** Creates a new user account and returns a JWT token.

**Request Body:**
```json
{
  "displayName": "John Doe",
  "email": "user@example.com",
  "password": "password123",
  "notifyOnDue": false
}
```

**Validations:**
- `displayName`: required, must be between 3 and 30 characters
- `email`: required, must be a valid email format
- `password`: required, minimum 8 characters
- `notifyOnDue`: optional, default is `false`

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "email": "user@example.com",
    "displayName": "John Doe",
    "notifyOnDue": false
  }
}
```

**Errors:**
- `400 Bad Request`: Validation error or email already exists

---

### 2. Login

**Endpoint:** `POST /api/auth/login`

**Authentication:** Not required

**Description:** Authenticates a user and returns a JWT token.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "email": "user@example.com",
    "displayName": "John Doe",
    "notifyOnDue": false
  }
}
```

**Errors:**
- `401 Unauthorized`: Invalid credentials

---

## User API

### 3. Update User Profile

**Endpoint:** `PUT /api/user`

**Authentication:** Required

**Description:** Updates the current user's profile information.

**Request Body:**
```json
{
  "displayName": "Jane Smith",
  "email": "newemail@example.com",
  "password": "newpassword123",
  "notifyOnDue": true
}
```

**Validations:**
- `displayName`: required, must be between 3 and 30 characters
- `email`: required, must be a valid email format
- `password`: required, minimum 8 characters
- `notifyOnDue`: optional

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "email": "newemail@example.com",
  "displayName": "Jane Smith",
  "notifyOnDue": true
}
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token
- `400 Bad Request`: Validation error

---

### 4. Update User Settings

**Endpoint:** `PUT /api/user/settings`

**Authentication:** Required

**Description:** Updates only the notification settings for the current user.

**Request Body:**
```json
{
  "notifyOnDue": true
}
```

**Response:** `200 OK`
```json
{
  "message": "Settings updated successfully"
}
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token

---

### 5. Delete User Account

**Endpoint:** `DELETE /api/user`

**Authentication:** Required

**Description:** Permanently deletes the current user's account.

**Response:** `200 OK`
```json
{
  "message": "User has been deleted"
}
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token

---

## Project API

### 6. Get Projects List

**Endpoint:** `GET /api/project`

**Authentication:** Required

**Description:** Returns all projects where the current user is a collaborator.

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "title": "Project Alpha",
    "creatorName": "John Doe",
    "collaborators": [
      {
        "userId": "507f1f77bcf86cd799439011",
        "displayName": "John Doe",
        "role": "creator"
      },
      {
        "userId": "507f1f77bcf86cd799439012",
        "displayName": "Jane Smith",
        "role": "member"
      }
    ],
    "phases": [
      {
        "name": "To Do",
        "order": 1
      },
      {
        "name": "In Progress",
        "order": 2
      }
    ],
    "labels": [
      {
        "name": "Bug",
        "color": "#FF0000"
      }
    ],
    "createdAt": "2025-11-13T10:30:00",
    "updatedAt": "2025-11-13T15:45:00"
  }
]
```

---

### 7. Create New Project

**Endpoint:** `POST /api/project`

**Authentication:** Required

**Description:** Creates a new project with the current user as creator.

**Request Body:**
```json
{
  "title": "New Project",
  "collaborators": [
    {
      "userId": "507f1f77bcf86cd799439012",
      "role": "admin"
    }
  ],
  "phases": [
    {
      "name": "To Do",
      "order": 1
    },
    {
      "name": "Done",
      "order": 2
    }
  ],
  "labels": [
    {
      "name": "Feature",
      "color": "#00FF00"
    }
  ]
}
```

**Validations:**
- `title`: required, not empty
- `phases`: required, minimum 1 phase, maximum 3 phases
- `collaborators`: optional, if empty or not present, only the creator is automatically added
- `labels`: optional

**Notes:**
- The creator is automatically added to the collaborators list with role "creator"
- The `joinedAt` field is automatically set by the server for all collaborators

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "title": "New Project",
  "creatorName": "John Doe",
  "collaborators": [
    {
      "userId": "507f1f77bcf86cd799439011",
      "displayName": "John Doe",
      "role": "creator"
    },
    {
      "userId": "507f1f77bcf86cd799439012",
      "displayName": "Jane Smith",
      "role": "admin"
    }
  ],
  "phases": [
    {
      "name": "To Do",
      "order": 1
    },
    {
      "name": "Done",
      "order": 2
    }
  ],
  "labels": [
    {
      "name": "Feature",
      "color": "#00FF00"
    }
  ],
  "createdAt": "2025-11-13T16:00:00",
  "updatedAt": "2025-11-13T16:00:00"
}
```

**Errors:**
- `400 Bad Request`: Validation error

---

### 8. Update Project

**Endpoint:** `PUT /api/project/{id}`

**Authentication:** Required

**Permissions:** Only the project creator can update it

**Path Parameters:**
- `id`: Project ID (ObjectId string)

**Request Body:** (all fields are optional)
```json
{
  "title": "Updated Title",
  "phases": [
    {
      "name": "Backlog",
      "order": 1
    }
  ],
  "labels": [
    {
      "name": "Priority",
      "color": "#FFA500"
    }
  ]
}
```

**Notes:**
- You can send only the fields you want to update
- Collaborators **CANNOT** be updated through this endpoint (use dedicated endpoints)

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "title": "Updated Title",
  "creatorName": "John Doe",
  "collaborators": [
    {
      "userId": "507f1f77bcf86cd799439011",
      "displayName": "John Doe",
      "role": "creator"
    }
  ],
  "phases": [
    {
      "name": "Backlog",
      "order": 1
    }
  ],
  "labels": [
    {
      "name": "Priority",
      "color": "#FFA500"
    }
  ],
  "createdAt": "2025-11-13T16:00:00",
  "updatedAt": "2025-11-13T16:30:00"
}
```

**Errors:**
- `404 Not Found`: Project not found
- `403 Forbidden`: User is not the project creator

---

### 9. Add Collaborator

**Endpoint:** `POST /api/project/{id}/collaborators`

**Authentication:** Required

**Permissions:** Only the project creator can add collaborators

**Path Parameters:**
- `id`: Project ID (ObjectId string)

**Request Body:**
```json
{
  "userId": "507f1f77bcf86cd799439013",
  "role": "member"
}
```

**Validations:**
- `userId`: required, not empty
- `role`: optional, default "member"

**Notes:**
- The `joinedAt` field is automatically set by the server
- Cannot add the same collaborator twice

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "title": "Project Alpha",
  "creatorName": "John Doe",
  "collaborators": [
    {
      "userId": "507f1f77bcf86cd799439011",
      "displayName": "John Doe",
      "role": "creator"
    },
    {
      "userId": "507f1f77bcf86cd799439013",
      "displayName": "Bob Wilson",
      "role": "member"
    }
  ],
  "phases": [...],
  "labels": [...],
  "createdAt": "2025-11-13T16:00:00",
  "updatedAt": "2025-11-13T17:00:00"
}
```

**Errors:**
- `404 Not Found`: Project not found
- `403 Forbidden`: User is not the project creator
- `400 Bad Request`: Collaborator already exists in the project

---

### 10. Remove Collaborator

**Endpoint:** `DELETE /api/project/{id}/collaborators/{collaboratorId}`

**Authentication:** Required

**Permissions:** Only the project creator can remove collaborators

**Path Parameters:**
- `id`: Project ID (ObjectId string)
- `collaboratorId`: User ID to remove (ObjectId string)

**Notes:**
- The creator cannot remove themselves

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "title": "Project Alpha",
  "creatorName": "John Doe",
  "collaborators": [
    {
      "userId": "507f1f77bcf86cd799439011",
      "displayName": "John Doe",
      "role": "creator"
    }
  ],
  "phases": [...],
  "labels": [...],
  "createdAt": "2025-11-13T16:00:00",
  "updatedAt": "2025-11-13T17:30:00"
}
```

**Errors:**
- `404 Not Found`: Project not found
- `403 Forbidden`: User is not the project creator
- `400 Bad Request`: Cannot remove the project creator

---

## Health Check API

### 11. Check MongoDB Connection

**Endpoint:** `GET /health`

**Authentication:** Required

**Description:** Checks if the MongoDB connection is working properly.

**Response:** `200 OK`
```
✅ MongoDB connection OK!
```

**Errors:**
- `500 Internal Server Error`: MongoDB connection failed

---

### 12. Check JWT Token

**Endpoint:** `GET /health/jwt`

**Authentication:** Required

**Description:** Verifies that the JWT token is valid and returns the user principal name.

**Response:** `200 OK`
```
user@example.com
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token

---

## HTTP Status Codes

- `200 OK`: Request executed successfully
- `400 Bad Request`: Validation error or invalid data
- `401 Unauthorized`: Authentication failed or invalid token
- `403 Forbidden`: User does not have necessary permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

---

## Error Format

All errors return a JSON object with the following format:

```json
{
  "title": "Error title",
  "status": 400,
  "detail": "Detailed error description"
}
```

For validation errors (constraint violations):

```json
{
  "title": "Constraint Violation",
  "status": 400,
  "violations": [
    {
      "field": "fieldName",
      "message": "Error message"
    }
  ]
}
```

---

## Data Models

### User
```json
{
  "id": "string (ObjectId)",
  "email": "string",
  "displayName": "string",
  "notifyOnDue": "boolean"
}
```

### Collaborator
```json
{
  "userId": "string (ObjectId)",
  "role": "string",
  "joinedAt": "datetime (ISO 8601)"
}
```

### Phase
```json
{
  "name": "string",
  "order": "integer"
}
```

### Label
```json
{
  "name": "string",
  "color": "string (hex color)"
}
```

### Project
```json
{
  "id": "string (ObjectId)",
  "title": "string",
  "creatorName": "string",
  "collaborators": "array of Collaborator",
  "phases": "array of Phase",
  "labels": "array of Label",
  "createdAt": "datetime (ISO 8601)",
  "updatedAt": "datetime (ISO 8601)"
}
```

---

## General Notes

1. **Dates and Times:** All datetime fields are in ISO 8601 format (e.g., `2025-11-13T16:00:00`)
2. **ObjectId:** MongoDB IDs are 24-character hexadecimal strings
3. **JWT Token:** JWT token has a duration of 24 hours (configurable)
4. **Collaborator Roles:** Supported roles are: `creator`, `admin`, `member`
5. **Limits:** Maximum 3 phases per project
6. **Password Security:** Passwords are hashed using bcrypt before storage
7. **Email Uniqueness:** Each email can only be registered once

---

## Usage Examples

### Complete Flow: User Registration and Project Creation

#### 1. Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "displayName": "John Doe",
    "email": "john@example.com",
    "password": "securepass123",
    "notifyOnDue": true
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "email": "john@example.com",
    "displayName": "John Doe",
    "notifyOnDue": true
  }
}
```

#### 2. Login (Alternative to Registration)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securepass123"
  }'
```

#### 3. Create a New Project
```bash
curl -X POST http://localhost:8080/api/project \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "title": "My Awesome Project",
    "phases": [
      {"name": "To Do", "order": 1},
      {"name": "In Progress", "order": 2},
      {"name": "Done", "order": 3}
    ],
    "labels": [
      {"name": "Bug", "color": "#FF0000"},
      {"name": "Feature", "color": "#00FF00"}
    ]
  }'
```

#### 4. Get All Projects
```bash
curl -X GET http://localhost:8080/api/project \
  -H "Authorization: Bearer <token>"
```

#### 5. Add a Collaborator to Project
```bash
curl -X POST http://localhost:8080/api/project/{projectId}/collaborators \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "userId": "507f1f77bcf86cd799439012",
    "role": "admin"
  }'
```

#### 6. Update Project
```bash
curl -X PUT http://localhost:8080/api/project/{projectId} \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "title": "Updated Project Title"
  }'
```

#### 7. Update User Settings
```bash
curl -X PUT http://localhost:8080/api/user/settings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "notifyOnDue": false
  }'
```

#### 8. Remove Collaborator from Project
```bash
curl -X DELETE http://localhost:8080/api/project/{projectId}/collaborators/{collaboratorId} \
  -H "Authorization: Bearer <token>"
```

#### 9. Delete User Account
```bash
curl -X DELETE http://localhost:8080/api/user \
  -H "Authorization: Bearer <token>"
```

#### 10. Check MongoDB Connection
```bash
curl -X GET http://localhost:8080/health \
  -H "Authorization: Bearer <token>"
```

---

## Security Considerations

1. **JWT Token Storage:** Store the JWT token securely on the client side (e.g., HttpOnly cookies or secure storage)
2. **HTTPS:** Always use HTTPS in production to encrypt data in transit
3. **Password Requirements:** Enforce strong passwords (minimum 8 characters)
4. **Token Expiration:** JWT tokens expire after 24 hours - implement token refresh logic
5. **Input Validation:** All inputs are validated on the server side
6. **Authorization:** Users can only access and modify resources they have permission for

---

## Rate Limiting

Currently, no rate limiting is implemented. Consider adding rate limiting in production to prevent abuse.

---

## Pagination

Currently, the `GET /api/project` endpoint returns all projects. Consider implementing pagination for large datasets in production.

---

**API Version:** 1.0  
**Last Updated:** November 13, 2025  
**Base URL:** `http://localhost:8080` (development)

