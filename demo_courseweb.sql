-- create database coursesellingweb_db;
use coursesellingweb_db;
DROP TABLE courses, course_videos, feedbacks,blogs,sliders, statistics, languages, fields, detail_orders, orders,coupons,coupon_conditions,employees,mentors ;
DROP TABLE coupons,coupon_conditions,employees,mentors;
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY, 
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL
);
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    address VARCHAR(255),
    account_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);
CREATE TABLE employees (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    position VARCHAR(50) NOT NULL, 
    account_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE mentors(
	id INT AUTO_INCREMENT 	PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    major VARCHAR(30) NOT NULL,
    experience VARCHAR(20) NOT NULL,
    employee_id INT,
    create_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
CREATE TABLE coupons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT,
    employee_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
CREATE TABLE coupon_conditions (
    id INT AUTO_INCREMENT PRIMARY KEY,
	coupon_id int,
    discount_value DECIMAL(10,2) NOT NULL, 
    expiration_date DATE,  
    minimum_order_value DECIMAL(10,2),
    FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE CASCADE
);


CREATE TABLE fields (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    employee_id INT,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
CREATE TABLE languages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,  
    description TEXT,
    employee_id INT,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    mentor_id int,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url varchar(255) not null,
    demo_video_url VARCHAR(255),  
    video_type ENUM('youtube', 'cloudinary'),
    field_id INT,
    language_id INT,
    employee_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (field_id) REFERENCES fields(id) ON DELETE CASCADE,
    FOREIGN KEY (language_id) REFERENCES languages(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (mentor_id) REFERENCES mentors(id) ON DELETE CASCADE
);
alter table courses
add column image_url varchar(255) not null;
CREATE TABLE course_videos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT,
    video_url VARCHAR(255) NOT NULL,  
    video_type ENUM('youtube', 'cloudinary'),
    title VARCHAR(255) NOT NULL,  
    description TEXT,  
    lesson_number INT NOT NULL,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
CREATE TABLE user_course (
    user_id INT, 
    course_id INT,  
    purchase_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
CREATE TABLE course_coupons (
    course_id INT,
    coupon_id INT,
    PRIMARY KEY (course_id, coupon_id),
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE CASCADE
);


CREATE TABLE feedbacks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    course_id INT,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    employee_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);


CREATE TABLE `orders` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10,2), 
    discount_amount DECIMAL(10,2),  
    final_price DECIMAL(10,2), 
    status VARCHAR(50) NOT NULL CHECK (status IN ('Pending', 'Paid', 'Cancelled')),
    coupon_id INT,  
    payment_method VARCHAR(20),
    active BOOLEAN,
    employee_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE SET NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
alter table orders
modify column final_price decimal(10,2);
CREATE TABLE detail_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    course_id INT,
    price DECIMAL(10,2) NOT NULL,  
    FOREIGN KEY (order_id) REFERENCES `orders`(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE statistics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    stat_date DATE NOT NULL, 
    total_sales DECIMAL(10,2) NOT NULL, 
    total_orders INT NOT NULL,  
    total_customers INT NOT NULL,  
    employee_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE TABLE blogs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content LONGTEXT NOT NULL,
    image_url varchar(255),
    employee_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE TABLE sliders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    employee_id INT,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
CREATE TABLE tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    token_type VARCHAR(50),
    expiration_date TIMESTAMP,
    refresh_expiration_date TIMESTAMP,
    revoked BOOLEAN NOT NULL DEFAULT 0,
    expired BOOLEAN NOT NULL DEFAULT 0,
    account_id INT,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
        ON DELETE CASCADE
);


-- INSERT---

-- mentor
insert into mentors (name, email, major, experience, employee_id)
values ('HowKteam','howkteam@gmail.com','Kỹ sư phần mềm','8 năm',2),
('TITV','titv1988@gmail.com','Kỹ sư phần mềm','10 năm',2),
('Hỏi Dân IT','hoidanit@gmail.com','CNTT','8 năm',2),
('Sơn Đặng','sondang@gmail.com','CNTT','13 năm',2),
('BroCode','brocode@gmail.com','Kỹ sư phần mềm','12 năm',2),
('CafeDev','cafedev@gmail.com','Kỹ sư phần mềm','11 năm',2);

-- lânguage
insert into mentors (name, description, employee_id)
values ('C Plus','Học C Plus',2),
('Reactjs','Học Reactjs',2),
('Javascript','Học javascript',2),
('NodeJs','Học NodeJs',2),
('Python','Học python',2),
('SQL','Học SQL',2),
('HTML-CSS','Học HTML-CSS',2),
('GIT','Học GIT',2);

-- fields

insert into fields(name, description, employee_id)
values ('AI','Trí tuệ nhân tạo',2),
('Mobile Dev','Phát triển ứng dụng di động cho các nền tảng như iOS và Android',2),
('Big data','phân tích dữ liệu lớn',2),
('UI/UX Design','Thiết kế giao diện người dùng và trải nghiệm người dùng sao cho thân thiện và trực quan.',2),
('IOT','Phát triển các giải pháp liên quan đến kết nối và giao tiếp giữa các thiết bị thông minh.',2),
('QA/Testing','Kiểm thử phần mềm để đảm bảo chất lượng, phát hiện lỗi và cải thiện hiệu năng ứng dụng.',2),
('BA','Tìm hiểu và phân tích các yêu cầu kinh doanh, hỗ trợ việc triển khai các giải pháp phần mềm phù hợp.',2);

-- course

 insert into courses (title, mentor_id,description, price,image_url, demo_video_url,video_type,field_id,language_id,employee_id,for_free)
 values (' Lập Trình Cơ Bản PYTHON',4,'Khóa học Lập trình Python: bao gồm tất cả nội dung kiến thức cơ bản về Python, Các thuật toán, Lập trình hướng đối tượng Python, Cấu trúc dữ liệu, Xử lý tập tin trong, Lập trình giao diện.
',699000,'python.jpa','https://www.youtube.com/watch?v=6I_ulPytQIc&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA','youtube',3,6,2,true),
('Khóa học lập trình C++ Cơ bản',3,'Khóa học lập trình C++ Cơ bản',499000,'c++.jpa','https://www.youtube.com/watch?v=WS05AU6YYm4&list=PL33lvabfss1xagFyyQPRcppjFKMQ7lvJM','youtube',7,10,2,true),
('Khóa học HTML	CSS cho người mới',7,'Khóa học HTML	CSS cho người mới',999000,'html-css.jpa','https://www.youtube.com/watch?v=2oCN2q1x3c4&list=PLZPZq0r_RZOOxqHgOzPyCzIl4AJjXbCYt&index=1','youtube',2,8,2,true),
('Khóa học Java cho người mới',7,'Khóa học Java cho người mới',1999000,'javacore.png','https://www.youtube.com/watch?v=NBIUbTddde4&list=PLZPZq0r_RZOMhCAyywfnYLlrjiVOkdAI1','youtube',1,1,2,true);

-- course_videos
insert into course_videos (course_id, video_url, video_type, title, description, lesson_number)
values(2,'https://www.youtube.com/watch?v=uh8SiLlyDq8&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=2','youtube','Giới thiệu về lập trình và một số lưu ý cho người mới học lập trình','Khóa học này cung cấp phần lớn kiến thức và kỹ năng về ngôn ngữ lập trình Python',1),
(2,'https://www.youtube.com/watch?v=rrs-bKmJsoc&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=3','youtube','Tạo ghi chú comment trong lập trình Python','Khóa học này cung cấp phần lớn kiến thức và kỹ năng về ngôn ngữ lập trình Python',2),
(2,'https://www.youtube.com/watch?v=16qD3hRRadc&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=4','youtube','Cách xuất dữ liệu bằng câu lệnh print trong python','Khóa học này cung cấp phần lớn kiến thức và kỹ năng về ngôn ngữ lập trình Python',3),
(2,'https://www.youtube.com/watch?v=ohmzrmh6a50&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=5','youtube','Cách nhập dữ liệu bằng lệnh input trong Lập trình Python','Khóa học này cung cấp phần lớn kiến thức và kỹ năng về ngôn ngữ lập trình Python',4),
(2,'https://www.youtube.com/watch?v=vSAIkPlm6t8&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=6','youtube','Biến, hằng số và từ khóa trong Lập trình Python','Khóa học này cung cấp phần lớn kiến thức và kỹ năng về ngôn ngữ lập trình Python',5),
(2,'https://www.youtube.com/watch?v=vfS2_6nl7AA&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=7','youtube','Kiểu dữ liệu cơ bản trong Lập trình Python','abc',6),
(2,'https://www.youtube.com/watch?v=zLrmG6tEBr4&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=8','youtube','Ép kiểu dữ liệu | Chuyển đổi kiểu dữ liệu trong Lập trình Python','abb',7),
(2,'https://www.youtube.com/watch?v=EsUFLZcOp2g&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=9','youtube','Các phép toán số học cơ bản trong Lập trình Python','aa',8),
(2,'https://www.youtube.com/watch?v=95Ual9dXSc0&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=10','youtube','Các toán tử so sánh và logic trong Lập trình Python','aca',9),
(2,'https://www.youtube.com/watch?v=h4VfDWFYwas&list=PLyxSzL3F7486SaHaQayPdKJUScVFh1UwA&index=11','youtube','Các toán tử gán trong lập trình Python','aaaa',10);


