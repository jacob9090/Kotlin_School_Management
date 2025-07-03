<?php
// Enable error logging (for development)
ini_set('display_errors', 1);
error_reporting(E_ALL);

// Set headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Utility: Clean all output buffers
function cleanOutput() {
    while (ob_get_level()) ob_end_clean();
}

// Utility: JSON response
function jsonResponse($data, $statusCode = 200) {
    cleanOutput();
    http_response_code($statusCode);
    echo json_encode($data);
    exit();
}

// Include required files
require_once 'Database.php';
require_once 'User.php';

try {
    // Verify request type
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        jsonResponse(["message" => "Method not allowed"], 405);
    }

    // Read and decode JSON input
    $input = file_get_contents("php://input");
    if (empty($input)) {
        jsonResponse(["message" => "Empty request body"], 400);
    }

    $data = json_decode($input);
    if (json_last_error() !== JSON_ERROR_NONE) {
        jsonResponse(["message" => "Malformed JSON", "error" => json_last_error_msg()], 400);
    }

    // Verify action
    if (!isset($data->action)) {
        jsonResponse(["message" => "Missing 'action' field"], 400);
    }

    // Setup DB & user object
    $db = (new Database())->getConnection();
    if (!$db) {
        jsonResponse(["message" => "Database connection failed"], 500);
    }

    $user = new User($db);

    // Handle register
    if ($data->action === 'register') {
        $required = ['first_name', 'last_name', 'email', 'password'];
        $missing = array_filter($required, fn($field) => empty($data->$field ?? null));

        if (!empty($missing)) {
            jsonResponse([
                "message" => "Missing required fields",
                "missing_fields" => array_values($missing)
            ], 400);
        }

        if ($user->emailExists($data->email)) {
            jsonResponse(["message" => "Email already registered"], 409);
        }

        // Set user fields
        $user->first_name = $data->first_name;
        $user->last_name = $data->last_name;
        $user->email = $data->email;
        $user->password = $data->password;
        $user->role = $data->role ?? 'user';
        $user->user_phone = $data->user_phone ?? '';
        $user->user_district = $data->user_district ?? '';
        $user->user_community = $data->user_community ?? '';
        $user->user_cooperative = $data->user_cooperative ?? '';
        $user->user_address = $data->user_address ?? '';

        if ($user->register()) {
            jsonResponse([
                "status" => "success",
                "message" => "User registered successfully"
            ]);
        } else {
            jsonResponse([
                "message" => "Registration failed",
                "status" => "error"
            ], 500);
        }
    }

    // Handle login (optional, if you add LoginRequest in Android)
    if ($data->action === 'login') {
        if (empty($data->email) || empty($data->password)) {
            jsonResponse(["message" => "Email and password required"], 400);
        }

        $user->email = $data->email;
        $user->password = $data->password;

        if ($user->login()) {
            jsonResponse([
                "status" => "success",
                "message" => "Login successful",
                "user" => $user
            ]);
        } else {
            jsonResponse(["message" => "Invalid credentials"], 401);
        }
    }

    // Fallback: Unknown action
    jsonResponse(["message" => "Unknown action: {$data->action}"], 400);

} catch (Throwable $e) {
    jsonResponse([
        "message" => "Unhandled server exception",
        "error" => $e->getMessage()
    ], 500);
}
