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
  "message": "User registered successfully",
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
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
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
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
        "id": "phase1",
        "title": "To Do",
        "position": 0
      },
      {
        "id": "phase2",
        "title": "In Progress",
        "position": 1
      }
    ],
    "labels": [
      {
        "id": "label1",
        "title": "Bug",
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
      "id": "phase1",
      "title": "To Do",
      "position": 0
    },
    {
      "id": "phase2",
      "title": "Done",
      "position": 1
    }
  ],
  "labels": [
    {
      "id": "label1",
      "title": "Feature",
      "color": "#00FF00"
    }
  ]
}
```

**Validations:**
- `title`: required, not empty
- `phases`: required, minimum 1 phase, maximum 3 phases
- `phases[].id`: required, not empty
- `phases[].title`: required, not empty
- `phases[].position`: required, cannot be negative
- `labels[].id`: required, not empty (if labels provided)
- `labels[].title`: required, not empty (if labels provided)
- `labels[].color`: required, not empty (if labels provided)
- `collaborators`: optional, can be empty or omitted

**Notes:**
- The creator is automatically added to the collaborators list with role "creator"
- The `joinedAt` field is automatically set by the server for all collaborators
- You don't need to pass `joinedAt` in the request

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
      "id": "phase1",
      "title": "To Do",
      "position": 0
    },
    {
      "id": "phase2",
      "title": "Done",
      "position": 1
    }
  ],
  "labels": [
    {
      "id": "label1",
      "title": "Feature",
      "color": "#00FF00"
    }
  ],
  "createdAt": "2025-11-13T16:00:00",
  "updatedAt": "2025-11-13T16:00:00"
}
```

**Errors:**
- `400 Bad Request`: Validation error (e.g., more than 3 phases)

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
      "id": "phase1",
      "title": "Backlog",
      "position": 0
    }
  ],
  "labels": [
    {
      "id": "label1",
      "title": "Priority",
      "color": "#FFA500"
    }
  ]
}
```

**Validations:**
- All fields are optional (nullable)
- `phases`: maximum 3 phases if provided

**Notes:**
- You can send only the fields you want to update
- Collaborators **CANNOT** be updated through this endpoint (use dedicated endpoints `/api/project/{id}/collaborators`)
- Null or omitted fields are ignored and keep their current values

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
      "id": "phase1",
      "title": "Backlog",
      "position": 0
    }
  ],
  "labels": [
    {
      "id": "label1",
      "title": "Priority",
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
- `400 Bad Request`: Validation error (e.g., more than 3 phases)

---

### 9. Delete Project

**Endpoint:** `DELETE /api/project/{id}`

**Authentication:** Required

**Permissions:** Only the project creator can delete it

**Path Parameters:**
- `id`: Project ID (ObjectId string)

**Response:** `200 OK`
```json
{
  "message": "Project deleted successfully"
}
```

**Errors:**
- `404 Not Found`: Project not found
- `403 Forbidden`: User is not the project creator

---

### 10. Add Collaborator

**Endpoint:** `POST /api/project/{id}/collaborators`

**Authentication:** Required

**Permissions:** Only the project creator can add collaborators

**Path Parameters:**
- `id`: Project ID (ObjectId string)

**Request Body:**
```json
{
  "userId": "507f1f77bcf86cd799439013"
}
```

**Validations:**
- `userId`: required, not empty

**Notes:**
- The `role` is automatically set to "member" by the server
- The `joinedAt` field is automatically set to the current timestamp
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

### 11. Remove Collaborator

**Endpoint:** `DELETE /api/project/{id}/collaborators`

**Authentication:** Required

**Permissions:** 
- Any collaborator can remove themselves from the project
- Only the project creator can remove other collaborators

**Path Parameters:**
- `id`: Project ID (ObjectId string)

**Request Body:**
```json
{
  "userId": "507f1f77bcf86cd799439013"
}
```

**Validations:**
- `userId`: required, not empty

**Permission Rules:**
1. **Self-removal**: Any collaborator can remove themselves by passing their own `userId`
2. **Remove others**: Only the project creator can remove other collaborators
3. **Creator protection**: The project creator cannot remove themselves

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
- `403 Forbidden`: User trying to remove another collaborator but is not the project creator
- `400 Bad Request`: The project creator cannot remove themselves

**Usage Examples:**

*Example 1: A member leaving the project*
```json
// User ID 507f1f77bcf86cd799439013 wants to leave the project
DELETE /api/project/507f1f77bcf86cd799439011/collaborators
{
  "userId": "507f1f77bcf86cd799439013"
}
// ✅ Success - user removed from project
```

*Example 2: Creator removing a member**
```json
// Creator (507f1f77bcf86cd799439011) wants to remove member 507f1f77bcf86cd799439013
DELETE /api/project/507f1f77bcf86cd799439011/collaborators
{
  "userId": "507f1f77bcf86cd799439013"
}
// ✅ Success - member removed by creator
```

*Example 3: Creator trying to leave (not allowed)**
```json
// Creator trying to remove themselves
DELETE /api/project/507f1f77bcf86cd799439011/collaborators
{
  "userId": "507f1f77bcf86cd799439011"
}
// ❌ Error: "The project creator cannot remove themselves"
```

---

## Task API

### 12. Get Tasks by Project

**Endpoint:** `GET /api/task/{projectId}`

**Authentication:** Required

**Description:** Returns all tasks for a specific project.

**Path Parameters:**
- `projectId`: Project ID (ObjectId string)

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439020",
    "projectId": "507f1f77bcf86cd799439011",
    "title": "Fix login bug",
    "description": "Users cannot login with special characters in password",
    "phaseId": "phase1",
    "labels": [
      {
        "id": "label1",
        "title": "Bug",
        "color": "#FF0000"
      },
      {
        "id": "label2",
        "title": "Priority",
        "color": "#FFA500"
      }
    ],
    "assignees": [
      {
        "userId": "507f1f77bcf86cd799439012",
        "displayName": "Jane Smith"
      }
    ],
    "createdBy": "507f1f77bcf86cd799439011",
    "createdByName": "John Doe",
    "dueDate": "2025-11-20T18:00:00",
    "createdAt": "2025-11-13T10:30:00",
    "updatedAt": "2025-11-13T15:45:00"
  }
]
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token
- `404 Not Found`: Project not found

---

### 13. Create Task

**Endpoint:** `POST /api/task/{projectId}`

**Authentication:** Required

**Description:** Creates a new task in the specified project.

**Path Parameters:**
- `projectId`: Project ID (ObjectId string)

**Request Body:**
```json
{
  "title": "Implement user profile page",
  "description": "Create a page where users can view and edit their profile information",
  "phaseId": "phase1",
  "labelIds": ["label1", "label2"],
  "assignees": ["507f1f77bcf86cd799439012", "507f1f77bcf86cd799439013"],
  "dueDate": "2025-11-25T18:00:00"
}
```

**Validations:**
- `title`: required, not blank
- `phaseId`: required, not blank
- `description`: optional
- `labelIds`: optional, array of label IDs (strings)
- `assignees`: optional, array of user IDs (strings)
- `dueDate`: optional, ISO 8601 format

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439021",
  "projectId": "507f1f77bcf86cd799439011",
  "title": "Implement user profile page",
  "description": "Create a page where users can view and edit their profile information",
  "phaseId": "phase1",
  "labels": [
    {
      "id": "label1",
      "title": "Feature",
      "color": "#00FF00"
    },
    {
      "id": "label2",
      "title": "Priority",
      "color": "#FFA500"
    }
  ],
  "assignees": [
    {
      "userId": "507f1f77bcf86cd799439012",
      "displayName": "Jane Smith"
    },
    {
      "userId": "507f1f77bcf86cd799439013",
      "displayName": "Bob Wilson"
    }
  ],
  "createdBy": "507f1f77bcf86cd799439011",
  "createdByName": "John Doe",
  "dueDate": "2025-11-25T18:00:00",
  "createdAt": "2025-11-16T10:00:00",
  "updatedAt": "2025-11-16T10:00:00"
}
```

**Errors:**
- `400 Bad Request`: Validation error
- `401 Unauthorized`: Invalid or missing token
- `404 Not Found`: Project not found

---

## Notification API

### 16. Get User Notifications

**Endpoint:** `GET /api/notifications`

**Authentication:** Required

**Description:** Returns all unread notifications plus the last 10 read notifications for the current user, sorted by creation date (newest first).

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439030",
    "recipientId": "507f1f77bcf86cd799439012",
    "senderId": "507f1f77bcf86cd799439011",
    "senderName": "John Doe",
    "type": "taskAssigned",
    "message": "John Doe ti ha assegnato il task: Fix login bug",
    "entityId": "507f1f77bcf86cd799439020",
    "entityType": "task",
    "isRead": false,
    "createdAt": "2025-11-16T14:30:00"
  },
  {
    "id": "507f1f77bcf86cd799439031",
    "recipientId": "507f1f77bcf86cd799439012",
    "senderId": "507f1f77bcf86cd799439011",
    "senderName": "John Doe",
    "type": "projectInvite",
    "message": "John Doe ti ha aggiunto al progetto: TaskFlow App",
    "entityId": "507f1f77bcf86cd799439010",
    "entityType": "project",
    "isRead": false,
    "createdAt": "2025-11-16T12:00:00"
  },
  {
    "id": "507f1f77bcf86cd799439032",
    "recipientId": "507f1f77bcf86cd799439012",
    "senderId": "507f1f77bcf86cd799439011",
    "senderName": "John Doe",
    "type": "taskAssigned",
    "message": "John Doe ti ha assegnato il task: Update documentation",
    "entityId": "507f1f77bcf86cd799439021",
    "entityType": "task",
    "isRead": true,
    "createdAt": "2025-11-15T10:00:00"
  }
]
```

**Notification Types:**
- `taskAssigned`: When a user is assigned to a task
- `taskCreated`: When a new task is created in a project (notifies all collaborators except the creator)
- `taskDueSoon`: When a task is due within 24 hours (notifies all assignees, checked every hour)
- `projectInvite`: When a user is added as a collaborator to a project

**Entity Types:**
- `task`: Notification related to a task
- `project`: Notification related to a project

**Errors:**
- `401 Unauthorized`: Invalid or missing token

---

### 17. Get Unread Notifications Count

**Endpoint:** `GET /api/notifications/count`

**Authentication:** Required

**Description:** Returns the count of unread notifications for the current user.

**Response:** `200 OK`
```json
{
  "unreadCount": 5
}
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token

---

### 18. Mark Notification as Read

**Endpoint:** `PUT /api/notifications/{id}/read`

**Authentication:** Required

**Description:** Marks a specific notification as read. Users can only mark their own notifications as read.

**Path Parameters:**
- `id`: Notification ID (ObjectId string)

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439030",
  "recipientId": "507f1f77bcf86cd799439012",
  "senderId": "507f1f77bcf86cd799439011",
  "senderName": "John Doe",
  "type": "taskAssigned",
  "message": "John Doe ti ha assegnato il task: Fix login bug",
  "entityId": "507f1f77bcf86cd799439020",
  "entityType": "task",
  "isRead": true,
  "createdAt": "2025-11-16T14:30:00"
}
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token
- `403 Forbidden`: User is not authorized to mark this notification as read
- `404 Not Found`: Notification not found

---

### 19. Mark All Notifications as Read

**Endpoint:** `PUT /api/notifications/read-all`

**Authentication:** Required

**Description:** Marks all unread notifications for the current user as read.

**Response:** `200 OK`
```json
{
  "message": "All notifications marked as read"
}
```

**Errors:**
- `401 Unauthorized`: Invalid or missing token

---

### Automatic Notifications

Notifications are automatically created in the following scenarios:

1. **Task Creation**: When a new task is created in a project
   - Recipients: All project collaborators (except the creator)
   - Type: `taskCreated`
   - Entity: Task ID
   - Message: "{Creator} ha creato una nuova task nel progetto: {Task Title}"

2. **Task Assignment**: When a user is assigned to a task (via create or update)
   - Recipients: All newly assigned users
   - Type: `taskAssigned`
   - Entity: Task ID
   - Message: "{Assigner} ti ha assegnato il task: {Task Title}"

3. **Task Due Soon**: When a task is due within 24 hours (checked every hour by scheduler)
   - Recipients: All task assignees
   - Type: `taskDueSoon`
   - Entity: Task ID
   - Message: "La task \"{Task Title}\" scadrà tra meno di 24 ore!"
   
4. **Task Deletion**: When a task is deleted, all related notifications are automatically removed

**Note:** Users do not receive notifications for actions they perform themselves (e.g., self-assigning a task).

---

## Health Check API

### 15. Check MongoDB Connection

**Endpoint:** `GET /health`

**Authentication:** Not required

**Description:** Checks if the backend can connect to MongoDB.

**Response:** `200 OK`
```json
{
  "status": "UP",
  "mongodb": "connected"
}
```

**Errors:**
- `503 Service Unavailable`: MongoDB connection failed

---

## Common Error Responses

### 400 Bad Request
```json
{
  "title": "Constraint Violation",
  "status": 400,
  "violations": [
    {
      "field": "email",
      "message": "must be a well-formed email address"
    }
  ]
}
```

### 401 Unauthorized
```json
{
  "message": "Invalid credentials"
}
```

### 403 Forbidden
```json
{
  "message": "You are not allowed to update this project"
}
```

### 404 Not Found
```json
{
  "message": "Project not found"
}
```

---

## Notes

- All timestamps are in ISO 8601 format (e.g., `2025-11-13T10:30:00`)
- ObjectId fields are represented as 24-character hexadecimal strings
- JWT tokens are valid for the duration specified in the server configuration
- The `joinedAt` field for collaborators is automatically managed by the server and should not be sent in requests
