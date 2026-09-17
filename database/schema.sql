DROP DATABASE IF EXISTS ai_research_platform;

-- Create the AI research platform database
CREATE DATABASE ai_research_platform
  CHARACTER SET utf8mb4                # sets default char encoding to utf8mb4
  COLLATE utf8mb4_unicode_ci;          # sets default collation for texts to utf8mb4_unicode_ci

USE ai_research_platform;
    
CREATE TABLE users (
    user_id      INT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL,
    role         ENUM('ADMIN', 'RESEARCHER') NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
    
CREATE TABLE profiles (
    profile_id   INT PRIMARY KEY AUTO_INCREMENT,
    user_id      INT NOT NULL UNIQUE,
    bio          TEXT,
    contact_info VARCHAR(150),
    FOREIGN KEY (user_id) REFERENCES users(user_id)    # only an existing user has profile
        ON DELETE CASCADE                              # if user deleted, delete his profile
);

CREATE TABLE resources (
     resource_id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(100) NOT NULL,
     type VARCHAR(50) NOT NULL,
     status ENUM('AVAILABLE','IN_USE','MAINTENANCE') NOT NULL,
     created_by INT NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (created_by) REFERENCES users(user_id)
);

CREATE TABLE projects (                                  # project details
     project_id INT PRIMARY KEY AUTO_INCREMENT,
     title VARCHAR(150) NOT NULL,
     description TEXT,
     status ENUM('ACTIVE','COMPLETED','ARCHIVED') NOT NULL,
     created_by INT NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (created_by) REFERENCES users(user_id)
);

CREATE TABLE project_team (
     project_id INT NOT NULL,
     user_id INT NOT NULL,
     PRIMARY KEY (project_id, user_id),
     FOREIGN KEY (project_id) REFERENCES projects(project_id)
         ON DELETE CASCADE,
	 FOREIGN KEY (user_id) REFERENCES users(user_id)
         ON DELETE CASCADE
);

CREATE TABLE datasets (
     dataset_id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(100)NOT NULL,
     description TEXT,
     file_path VARCHAR(255) NOT NULL,
     uploaded_by INT NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (uploaded_by) REFERENCES users(user_id)
);

CREATE TABLE models (
    model_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    dataset_id INT NOT NULL,
    parameters TEXT,
    training_status ENUM('PENDING','TRAINING','COMPLETE','FAILED') NOT NULL,
    progress_percent INT NOT NULL DEFAULT 0,
    researcher_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dataset_id) REFERENCES datasets(dataset_id),
    FOREIGN KEY (researcher_id) REFERENCES users(user_id)
);

CREATE TABLE experiments (
    experiment_id INT PRIMARY KEY AUTO_INCREMENT,
    model_id INT NOT NULL,
    parameters TEXT,
    result TEXT,
    performance_metric VARCHAR(50),
    researcher_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (model_id) REFERENCES models(model_id),
    FOREIGN KEY (researcher_id) REFERENCES users(user_id)
);

CREATE TABLE collaborations (
    collaboration_id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    initiated_by INT NOT NULL,
    status ENUM('PENDING','ACTIVE','CLOSED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(project_id),
    FOREIGN KEY (initiated_by) REFERENCES users(user_id)
);

CREATE TABLE usage_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    resource_id INT NOT NULL,
    user_id INT NOT NULL,
    usage_detail TEXT,
    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

SHOW TABLES;