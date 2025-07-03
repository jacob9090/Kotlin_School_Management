<?php
class Student {
    public $id;
    public $user_id;
    public $first_name;
    public $last_name;
    public $gender;
    public $dob;
    public $nationality;
    public $language_preferences;
    public $photo;
    public $cert_id;
    public $email;
    public $student_phone;
    public $password;
    public $admission_date;
    public $current_class;
    public $previous_school;
    public $document_name;
    public $cert_photo;
    public $health_conditions;
    public $on_medications;
    public $blood_type;
    public $take_immunization;
    public $dietary_restrictions;
    public $restricted_diets;
    public $has_disability;
    public $disability_type;
    public $fathers_name;
    public $fathers_occupation;
    public $fathers_contact;
    public $mothers_name;
    public $mothers_occupation;
    public $mothers_contact;
    public $guardians_name;
    public $guardians_occupation;
    public $guardians_contact;
    public $on_create;
    public $on_update;

    private $pdo;

    public function __construct(PDO $pdo) {
        $this->pdo = $pdo;
    }

    public function getAll(): array {
        $stmt = $this->pdo->query("SELECT * FROM students");
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public function upsert(array $data) {
        $columns = array_keys($data);
        $placeholders = array_map(fn($c) => ":$c", $columns);

        $updates = [];
        foreach ($columns as $column) {
            if ($column !== 'id') {
                $updates[] = "$column = VALUES($column)";
            }
        }

        $sql = sprintf(
            'INSERT INTO students (%s) VALUES (%s) ON DUPLICATE KEY UPDATE %s',
            implode(',', $columns),
            implode(',', $placeholders),
            implode(',', $updates)
        );

        $stmt = $this->pdo->prepare($sql);
        foreach ($data as $key => $value) {
            $stmt->bindValue(":$key", $value);
        }
        $stmt->execute();
        return $this->pdo->lastInsertId();
    }
}