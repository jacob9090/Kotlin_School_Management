<?php
class User {
    private $conn;
    private $table_name = "fedco_users";

    public $id;
    public $first_name;
    public $last_name;
    public $role;
    public $survey_access;
    public $user_phone;
    public $user_district;
    public $user_community;
    public $user_cooperative;
    public $user_address;
    public $user_photo;
    public $email;
    public $password;
    public $is_active;

    public function __construct($db) {
        $this->conn = $db;
    }

    public function register() {
        $query = "INSERT INTO " . $this->table_name . " 
                SET first_name=:first_name, last_name=:last_name, role=:role, 
                user_phone=:user_phone, user_district=:user_district, 
                user_community=:user_community, user_cooperative=:user_cooperative, 
                user_address=:user_address, email=:email, password=:password";
        
        $stmt = $this->conn->prepare($query);
        
        // Sanitize inputs
        $this->first_name = htmlspecialchars(strip_tags($this->first_name));
        $this->last_name = htmlspecialchars(strip_tags($this->last_name));
        $this->role = htmlspecialchars(strip_tags($this->role));
        $this->user_phone = htmlspecialchars(strip_tags($this->user_phone));
        $this->user_district = htmlspecialchars(strip_tags($this->user_district));
        $this->user_community = htmlspecialchars(strip_tags($this->user_community));
        $this->user_cooperative = htmlspecialchars(strip_tags($this->user_cooperative));
        $this->user_address = htmlspecialchars(strip_tags($this->user_address));
        $this->email = htmlspecialchars(strip_tags($this->email));
        $this->password = htmlspecialchars(strip_tags($this->password));
        
        // Hash password
        $this->password = password_hash($this->password, PASSWORD_BCRYPT);
        
        // Bind parameters
        $stmt->bindParam(":first_name", $this->first_name);
        $stmt->bindParam(":last_name", $this->last_name);
        $stmt->bindParam(":role", $this->role);
        $stmt->bindParam(":user_phone", $this->user_phone);
        $stmt->bindParam(":user_district", $this->user_district);
        $stmt->bindParam(":user_community", $this->user_community);
        $stmt->bindParam(":user_cooperative", $this->user_cooperative);
        $stmt->bindParam(":user_address", $this->user_address);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":password", $this->password);
        
        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function login() {
        $query = "SELECT * FROM " . $this->table_name . " WHERE email = ? LIMIT 0,1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->email);
        $stmt->execute();
        $num = $stmt->rowCount();
        
        if($num > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            
            if(password_verify($this->password, $row['password'])) {
                $this->id = $row['id'];
                $this->first_name = $row['first_name'];
                $this->last_name = $row['last_name'];
                $this->role = $row['role'];
                $this->email = $row['email'];
                $this->user_phone = $row['user_phone'];
                $this->user_district = $row['user_district'];
                $this->user_community = $row['user_community'];
                $this->user_cooperative = $row['user_cooperative'];
                $this->user_address = $row['user_address'];
                $this->user_photo = $row['user_photo'];
                
                return true;
            }
        }
        return false;
    }

    public function getUserDetails($user_id) {
        $query = "SELECT id, first_name, last_name, role, user_phone, user_district, 
                 user_community, user_cooperative, user_address, user_photo, email 
                 FROM " . $this->table_name . " WHERE id = ? LIMIT 0,1";
        
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $user_id);
        $stmt->execute();
        
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }
    
        public function emailExists($email) {
        $query = "SELECT id FROM " . $this->table_name . " WHERE email = ? LIMIT 1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $email);
        $stmt->execute();
        return $stmt->rowCount() > 0;
    }
    
}
?>