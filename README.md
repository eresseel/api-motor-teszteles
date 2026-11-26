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
│               └── DeviceListTest.java
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

### TC-01 – Eszközlista sikeres lekérdezése

Kérés:

```http
GET /api/devices
```

Ellenőrzések:

- HTTP státuszkód: `200`
- A válasz JSON tömb.
- A válasz legalább egy elemet tartalmaz.

### TC-02 – Listaelemek alapmezőinek ellenőrzése

Kérés:

```http
GET /api/devices
```

Ellenőrzések:

- HTTP státuszkód: `200`
- A válasz JSON tömb és nem üres.
- Az első elem tartalmazza az `id`, `name` és `category` mezőket.
- Az `id` numerikus (`Number`).
- A `name` és `category` szöveges (`String`).

A tesztek a dokumentált elvárásokhoz igazodnak. Nem feltételeznek konkrét eszköznevet, kategóriát vagy kezdő ID-t.
