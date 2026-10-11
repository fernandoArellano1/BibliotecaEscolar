USE biblioteca_db;

INSERT INTO categorias (nombre) VALUES
('Ficción'), ('Ciencia'), ('Historia'), ('Matemáticas');

INSERT INTO usuarios (nombre, rut, correo, contrasena, rol) VALUES
('Admin Bibliotecario', '11111111-1', 'admin@biblioteca.cl', 'admin123', 'BIBLIOTECARIO'),
('Carlos Fuentes', '22222222-2', 'carlos@estudiante.cl', 'user123', 'ESTUDIANTE');

INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES
('Juan Pérez', '22222222-2', '3º Medio A', 'juan@estudiante.cl'),
('María Gómez', '33333333-3', '4º Medio B', 'maria@estudiante.cl');

INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES
('Cien Años de Soledad', 'Gabriel García Márquez', '978-0307474728', 'Sudamericana', 5, 1),
('Breve Historia del Tiempo', 'Stephen Hawking', '978-0553380163', 'Bantam', 3, 2),
('Álgebra de Baldor', 'Aurelio Baldor', '978-9708110297', 'Patria', 10, 4);

INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES
(1, 1, CURDATE(), NULL, FALSE);