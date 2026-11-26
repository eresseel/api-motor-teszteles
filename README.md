# API-MOTOR – Java API tesztek

Maven-alapú automatizált tesztprojekt az API-MOTOR REST API-hoz.

## Technológia

- Java 17
- Maven
- JUnit 5
- REST Assured
- Alapértelmezett API URL: `http://localhost:18080`

## Projektstruktúra

```text
api-motor-java-tests/
├── src/
│   └── test/
│       └── java/
│           └── hu/laci/apimotor/
│               ├── ApiTestConfig.java
│               ├── DeviceListTest.java
│               ├── DeviceGetByIdTest.java
│               ├── DeviceMultiGetTest.java
│               ├── DeviceCreateAndUpdateTest.java
│               └── DeviceCreateAndDeleteTest.java
├── .gitignore
├── pom.xml
└── README.md
```

## Előfeltételek

- JDK 17 vagy újabb
- Maven 3.9 vagy újabb
- Futó API-MOTOR példány a `http://localhost:18080` címen

Ellenőrzés PowerShellben:

```powershell
java -version
mvn -version
```

## API indítása Windows alatt

Abban a könyvtárban, ahol az `api-motor.jar` található:

```powershell
C:\Users\Vizsga\Desktop\API szerver\Motor\api-motor.bat
```

Az API-t futva kell hagyni. A teszteket egy másik PowerShell ablakból indítsd.

Ellenőrizd az API állapotát:

```powershell
Invoke-RestMethod http://localhost:18080/api/health
```

Elvárt válasz:

```json
{
  "status": "UP"
}
```

## Tesztek futtatása

A projekt gyökérkönyvtárában:

```powershell
cmd /c "C:\maven\bin\mvn.cmd clean test"
```

A teszteredmények helye:

```text
target/surefire-reports/
```

## Implementált tesztesetek

---

### 1. tesztcsomag – Eszközlista lekérdezése

#### TC-01 – Eszközlista sikeres lekérdezése

Kérés:

```http
GET /api/devices
```

Ellenőrzések:

- HTTP státuszkód: `200`
- A válasz JSON tömb.
- A válasz legalább egy elemet tartalmaz.

Manuális ellenőrzés PowerShellben:

```powershell
Invoke-RestMethod http://localhost:18080/api/devices
```

---

#### TC-02 – Listaelemek alapmezőinek ellenőrzése

Kérés:

```http
GET /api/devices
```

Ellenőrzések:

- HTTP státuszkód: `200`
- A válasz JSON tömb és nem üres.
- Az első elem tartalmazza az `id`, `name` és `category` mezőket.
- Az `id` numerikus érték.
- A `name` és `category` szöveges érték.

Manuális ellenőrzés PowerShellben:

```powershell
Invoke-RestMethod http://localhost:18080/api/devices
```

---

### 2. tesztcsomag – Egy adott eszköz lekérdezése

#### TC-03 – Létező eszköz lekérdezése azonosító alapján

Kérés:

```http
GET /api/devices/2
```

Ellenőrzések:

- HTTP státuszkód: `200`
- Az `id` mező értéke: `2`
- A `name` mező értéke: `Dell UltraSharp U2723QE`
- A `category` mező értéke: `monitor`
- A válasz tartalmaz `data` objektumot.
- A `data.year` értéke: `2023`
- A `data.price` értéke: `229000`

Manuális ellenőrzés PowerShellben:

```powershell
Invoke-RestMethod http://localhost:18080/api/devices/2
```

---

#### TC-04 – Nem létező eszköz lekérdezése (negatív teszt)

Kérés:

```http
GET /api/devices/999999
```

Ellenőrzések:

- HTTP státuszkód: `404`
- Az `error` mező értéke: `Not Found`
- A `message` mező értéke: `Device eszköz a megadott azonosítóval 999999 nem található.`

Manuális ellenőrzés PowerShellben:

```powershell
try {
    Invoke-RestMethod http://localhost:18080/api/devices/999999
} catch {
    $_.Exception.Response.StatusCode.value__
    $_.ErrorDetails.Message
}
```

---

### 3. tesztcsomag – Több megadott eszköz lekérdezése

#### TC-05 – Több eszköz lekérdezése query paraméterrel

Kérés:

```http
GET /api/devices?id=1&id=3
```

Ellenőrzések:

- HTTP státuszkód: `200`
- A válasz JSON tömb.
- A válaszban szerepel az `id=1` azonosítójú eszköz.
- A válaszban szerepel az `id=3` azonosítójú eszköz.
- A válaszban pontosan 2 elem szerepel.

Manuális ellenőrzés PowerShellben:

```powershell
Invoke-RestMethod "http://localhost:18080/api/devices?id=1&id=3"
```

---

#### TC-06 – Nem létező azonosító kezelése query paraméteres lekérdezésben

Kérés:

```http
GET /api/devices?id=1&id=999999
```

Ellenőrzések:

- HTTP státuszkód: `200`
- A válaszban szerepel az `id=1` azonosítójú eszköz.
- A válaszban nem szerepel `id=999999` azonosítójú eszköz.
- A válaszban pontosan 1 elem szerepel.

Manuális ellenőrzés PowerShellben:

```powershell
Invoke-RestMethod "http://localhost:18080/api/devices?id=1&id=999999"
```

---

### 4. tesztcsomag – Új eszköz létrehozása és módosítása

#### TC-07 – Új eszköz létrehozása

Kérés:

```http
POST /api/devices
Content-Type: application/json

{
  "name": "Test Notebook",
  "category": "laptop",
  "data": {
    "year": 2026,
    "price": 350000,
    "available": true
  }
}
```

Ellenőrzések:

- HTTP státuszkód: `201`
- A válasz tartalmaz `id` mezőt (numerikus).
- A `name` mező értéke: `Test Notebook`
- A `category` mező értéke: `laptop`
- A válasz tartalmaz `data` objektumot.
- A válasz tartalmaz `createdAt` mezőt.

Manuális ellenőrzés PowerShellben:

```powershell
$body = '{"name":"Test Notebook","category":"laptop","data":{"year":2026,"price":350000,"available":true}}'
Invoke-RestMethod -Method Post -Uri http://localhost:18080/api/devices -ContentType "application/json" -Body $body
```

---

#### TC-08 – Létrehozott eszköz módosítása

Előfeltétel: TC-07 lefutott, a válaszból kiolvasott `id` értékét `{id}`-vel jelöljük.

Kérés:

```http
PUT /api/devices/{id}
Content-Type: application/json

{
  "name": "Updated Test Notebook",
  "category": "laptop",
  "data": {
    "year": 2026,
    "price": 325000,
    "available": false
  }
}
```

Ellenőrzések:

- HTTP státuszkód: `200`
- Az `id` mező megegyezik a létrehozott eszköz azonosítójával.
- A `name` mező értéke: `Updated Test Notebook`
- A `category` mező értéke: `laptop`
- A `data.price` értéke: `325000`
- A `data.available` értéke: `false`
- A válasz tartalmaz `updatedAt` mezőt.

Manuális ellenőrzés PowerShellben (az `{id}` helyére a TC-07 válaszában kapott értéket kell írni):

```powershell
$body = '{"name":"Updated Test Notebook","category":"laptop","data":{"year":2026,"price":325000,"available":false}}'
Invoke-RestMethod -Method Put -Uri http://localhost:18080/api/devices/{id} -ContentType "application/json" -Body $body
```

---

### 5. tesztcsomag – Eszköz törlése

#### TC-09 – Létrehozott eszköz törlése

Előfeltétel: A teszt maga hoz létre egy új eszközt POST-tal, majd törli azt. A kapott `id` értékét `{id}`-vel jelöljük.

Kérés:

```http
DELETE /api/devices/{id}
```

Ellenőrzések:

- HTTP státuszkód: `200`
- A `message` mező tartalmazza a törölt eszköz azonosítóját.

Manuális ellenőrzés PowerShellben:

```powershell
# Először hozz létre egy eszközt, jegyezd meg az id-t:
$body = '{"name":"Temp Device","category":"laptop","data":{"year":2026,"price":100000,"available":true}}'
$created = Invoke-RestMethod -Method Post -Uri http://localhost:18080/api/devices -ContentType "application/json" -Body $body
$id = $created.id

# Majd töröld:
Invoke-RestMethod -Method Delete -Uri "http://localhost:18080/api/devices/$id"
```

---

#### TC-10 – Törölt eszköz nem kérdezhető le

Előfeltétel: TC-09 lefutott, az ott törölt `id` értékét `{id}`-vel jelöljük.

Kérés:

```http
GET /api/devices/{id}
```

Ellenőrzések:

- HTTP státuszkód: `404`
- Az `error` mező értéke: `Not Found`
- A `message` mező tartalmazza az azonosítót.

Manuális ellenőrzés PowerShellben (a TC-09-ben törölt `$id` változót használva):

```powershell
try {
    Invoke-RestMethod "http://localhost:18080/api/devices/$id"
} catch {
    $_.Exception.Response.StatusCode.value__
    $_.ErrorDetails.Message
}
```

---

## Megjegyzések

- A Maven az első futtatáskor letölti a szükséges függőségeket, ezért internetkapcsolat szükséges.
- Az API-t nem a Maven indítja; azt külön kell futtatni.
- A tesztprojekt nem módosítja az API forráskódját.
- A TC-07/TC-08 és TC-09/TC-10 tesztesetek egymástól függetlenek.
- Az API memóriaalapú: újraindítás után a POST/PUT/DELETE műveletek hatása elvész, az eredeti adatok visszaállnak.
