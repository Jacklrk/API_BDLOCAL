# 📚 App de Libros – Consulta en línea y guardado local

Esta aplicación Android permite a los usuarios buscar libros utilizando la API pública de [Open Library](https://openlibrary.org/developers/api), y guardar manualmente aquellos libros que deseen conservar para ver sin conexión.

---

## 🚀 Funcionalidades principales

- 🔍 **Búsqueda de libros** por título o autor usando la API de Open Library.
- 💾 **Guardado local de libros seleccionados** mediante Room (base de datos local).
- 🌐 Consulta dinámica desde red o almacenamiento interno.
- 📱 Interfaz adaptada con navegación inferior para cambiar entre "Buscar" y "Guardados".

---

## ⚙️ Tecnologías utilizadas

- Kotlin
- Retrofit2
- Room (SQLite)
- ViewBinding
- Material Components
- Coroutines (`lifecycleScope`)
- BottomNavigationView + Toolbar

---

## 🔗 Consumo de la API Open Library

Se utiliza el siguiente endpoint:
La app obtiene datos como:
- `title` (título del libro)
- `author_name` (autor)
- `key` (clave única del libro)

> Solo se procesan los campos esenciales para mostrar en la interfaz.

---

## 💾 Guardado local (Room)

La app usa Room para guardar libros seleccionados por el usuario:

### ¿Cómo se guarda un libro?

1. El usuario realiza una búsqueda.
2. Al tocar un libro, se muestra un diálogo:
   > "¿Deseas guardar este libro para verlo sin conexión?"
3. Si acepta, se guarda en la base de datos local.

### ¿Cómo se consulta sin internet?

- Al seleccionar la pestaña **"Guardados"**, se carga la lista desde Room.
- Si no hay libros guardados, no se muestra ningún resultado.

---

## 🧪 Estructura del proyecto

---

## 📝 Notas

- La clave del libro (`key`) es usada como identificador único en la base de datos.
- Si el libro no tiene autor, se muestra "Desconocido".
- La base de datos se mantiene persistente entre sesiones.

---

## 📸 Capturas

| Buscar libros                        | Guardar libro                       | Libros guardados                 |
|-------------------------------------|-------------------------------------|----------------------------------|
| ![](docs/screenshot_buscar.png)    | ![](docs/screenshot_guardar.png)   | ![](docs/screenshot_guardados.png) |

---

## 📌 TODO futuro

- Agregar favoritos o eliminar guardados.
- Mostrar detalles extendidos del libro.
- Mejorar diseño visual con Material 3.
