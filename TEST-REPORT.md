# Vezetői tesztjelentés – API-MOTOR REST API

**Projekt:** API-MOTOR informatikai eszköznyilvántartó  
**Verzió:** 1.0.0-SNAPSHOT  
**Készítette:** Laci  
**Futtatás dátuma:** 2026-11-26  
**Futtatási környezet:** Windows, JDK 17, Maven 3.9, localhost:18080  

---

## Összefoglalás

| Mutató | Érték |
|---|---|
| Összes teszteset | 10 |
| Sikeres | 10 |
| Sikertelen | 0 |
| Kihagyott | 0 |
| Sikerességi arány | 100% |

---

## Tesztcsomagonkénti eredmények

### 1. tesztcsomag – Eszközlista lekérdezése
| Azonosító | Leírás | Eredmény |
|---|---|---|
| TC-01 | Eszközlista sikeres lekérdezése | ✅ PASS |
| TC-02 | Listaelemek alapmezőinek ellenőrzése | ✅ PASS |

### 2. tesztcsomag – Egy adott eszköz lekérdezése
| Azonosító | Leírás | Eredmény |
|---|---|---|
| TC-03 | Létező eszköz lekérdezése azonosító alapján | ✅ PASS |
| TC-04 | Nem létező eszköz lekérdezése (negatív teszt) | ✅ PASS |

### 3. tesztcsomag – Több megadott eszköz lekérdezése
| Azonosító | Leírás | Eredmény |
|---|---|---|
| TC-05 | Több eszköz lekérdezése query paraméterrel | ✅ PASS |
| TC-06 | Nem létező azonosító kezelése query paraméteres lekérdezésben | ✅ PASS |

### 4. tesztcsomag – Új eszköz létrehozása és módosítása
| Azonosító | Leírás | Eredmény |
|---|---|---|
| TC-07 | Új eszköz létrehozása | ✅ PASS |
| TC-08 | Létrehozott eszköz módosítása | ✅ PASS |

### 5. tesztcsomag – Új eszköz létrehozása és törlése
| Azonosító | Leírás | Eredmény |
|---|---|---|
| TC-09 | Létrehozott eszköz törlése | ✅ PASS |
| TC-10 | Törölt eszköz nem kérdezhető le | ✅ PASS |

---

## Lefedett végpontok

| Metódus | Végpont | Teszteli |
|---|---|---|
| GET | `/api/devices` | TC-01, TC-02, TC-05, TC-06 |
| GET | `/api/devices/{id}` | TC-03, TC-04, TC-10 |
| POST | `/api/devices` | TC-07, TC-08, TC-09, TC-10 |
| PUT | `/api/devices/{id}` | TC-08 |
| DELETE | `/api/devices/{id}` | TC-09, TC-10 |

---

## Megjegyzések

- Minden teszteset egymástól függetlenül futtatható.
- Az állapotmódosító tesztek (POST, PUT, DELETE) saját tesztadatot hoznak létre futás közben.
- Az API memóriaalapú; a tesztadatok újraindítás után elvesznek, ez a tesztek eredményét nem befolyásolja.
- A részletes technikai jelentés a `target/surefire-reports/` mappában található.
