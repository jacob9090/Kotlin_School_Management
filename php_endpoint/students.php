<?php
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/Student.php';
require_once __DIR__ . '/utils.php';

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    exit;
}

$studentModel = new Student($pdo);

switch ($_SERVER['REQUEST_METHOD']) {
    case 'GET':
        $data = $studentModel->getAll();
        echo jsonResponse($data);
        break;
    case 'POST':
        $input = json_decode(file_get_contents('php://input'), true) ?: [];
        $studentModel->upsert($input);
        echo jsonResponse(['status' => 'ok']);
        break;
    default:
        http_response_code(405);
        echo jsonResponse(['error' => 'Method Not Allowed']);
}