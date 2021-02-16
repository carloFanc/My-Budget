# My-Budget 
Quest’app Android permette di tenere traccia delle entrate e uscite di carattere monetario dall’utente. Questa applicazione permette di:
1. Aggiungere informazioni riguardante un entrata/uscita
2. Aggiungere portafogli per la gestione del budget
3. Aggiungere categorie per definire il tipo di entrata/uscita
4. Pianificare transazioni ricorrenti che si aggiungono ogni n°giorni
5. Visualizzare statistiche riguardo le transazioni per categoria
6. Visualizzare la cronologia della transazioni avvenute
7. Visualizzare la mappa con le posizioni nelle quali sono avvenute le transazioni
8. Esportare il pdf con le transazioni
 
## Requisiti e dipendenze
La versione minima supportata corrisponde alla API 19 (Android 4.4– KitKat).
L'ambiente di sviluppo utilizzato è Android Studio 2.3.
La CompileSdkVersion corrisponde alle API 24.
Le dipendenze sono:
- com.android.support:appcombat-v7:24.2.1
- com.android.support:design:24.2.1
- com.android.support:cardview-v7:24.2.1
- com.android.support:gridlayout-v7:24.2.1
- com.google.android.gms;play-services-maps:10.0.1
- com.github.PhilJay:MPAndroidChart:v2.1.6
- com.itextpdf:itextg:5.5.10

## Requisiti e dipendenze
Il testing dell’applicazione è stato effettuato su: il mio dispositivo android personale Xiaomi Redmi Note 3 Pro, un Samsung Galaxy S6 e l’emulatore di Android Studio con il Nexus 5.

## ER Model
![unosututti](https://user-images.githubusercontent.com/18367371/108080555-4a4adb80-7070-11eb-957c-05d718ec7ed9.PNG)

## Database
I dati vengono salvati localmente attraverso l’utilizzo del database SQLite.
Il database utilizzato è composto da 4 tabelle descritte qui di seguito:

- PORTFOLIOS: tabella contenente informazioni riguardanti i portafogli
I Dati inseriti sono id, titolo, descrizione e flag per determinare se è settato.

- CATEGORIES: tabella contenente informazioni riguardanti le categorie
I Dati inseriti sono id, titolo.

- CASHFLOW: tabella contenente informazioni riguardanti le transazioni
I Dati inseriti sono id, quantità, portafoglio corrispondente, categoria,
descrizione, data, luogo.

- RECURRENT: tabella contenente informazioni riguardanti le transazioni ricorrenti
I dati inseriti sono gli stessi della tabella PORTFOLIOS. In più è stato aggiunto il campo
NTIMES che tiene in considerazione ogni quanti giorni si deve aggiungere un record In PORTFOLIOS.

## Funzionalità

### Il menu principale
Permette di spostarsi all’interno dell’applicazione. In ogni activity è presente l’Hamburger Button che permette di visualizzare il menù.
STUDIO RICHIESTO: Implementare BaseActivity che estende le altre Activity che compongono l’app

### Aggiungere informazioni riguardante la transazione
Utilizzata toolbar per gestire informazioni principali come data e bilancio del giorno, settimana e mese.
Queste informazioni cambiano in base alla Tab scelta ( TODAY-WEEK-MONTH ).
Le Tab sono collegate a 3 Fragment con una ListView che prende gli elementi della tabella corrispondente nel database.
Con il f.a.b. è possibile aggiungere una transazione.
STUDIO RICHIESTO:Gestione della toolbar, Uso dei Fragment e della ListView

### Aggiungere portafogli per la gestione del budget
Utilizzata ListView per visualizzare i portafogli utilizzabili all’interno dell’app.
E’ possibile aggiungere/editare/eliminare un portafoglio.
La ListView implementa un adapter customizzato principalmente per poter visualizzare la imageview ( SET ) solamente nel portafoglio settato.
STUDIO RICHIESTO: Adapter per ListView

### Aggiungere categorie per definire il tipo di ent./usc.
Utilizzata ListView per visualizzare le categorie utilizzabili all’interno dell’ inserimento di una transazione.
E’ possibile aggiungere/editare/eliminare una categoria. 

### Pianificare transazioni ricorrenti ogni n giorni
Utilizzata ListView per visualizzare le transazioni ricorrenti che dovranno permettere l’aggiunta automatica di transazioni ogni n° giorni all’interno dell’app.
E’ possibile aggiungere/editare/eliminare una transazione ricorrente. 
STUDIO RICHIESTO: Metodo per aggiungere le transazioni in automatico

### Visualizzare statistiche riguardo le transazioni per categoria
Utilizzato CardView contenente un Search per determinare quali date scegliere per visualizzare il grafico contenente informazioni del totale (in percentuale) a cui ammontano le transazioni per ogni categoria. E’ stato possibile implementare il grafico grazie ad una libreria esterna. 
STUDIO RICHIESTO:Libreria esterna 

### Visualizzare la History delle transazioni avvenute
Utilizzato CardView contenente un Search per determinare quali date scegliere per visualizzare la ListView che permette di vedere quali transazioni sono state effettuate in determinate date che non si trovano nell’Activity principale. 
Non è possibile aggiungere, modificare o eliminare le transazioni visualizzabili nella History.

### Visualizzare la mappa con le location delle transazioni
Utilizzato CardView contenente un Search per determinare quali date scegliere per visualizzare una mappa che mostra le location ( con le relative informazioni ) per ogni transazione. E’ stato possibile implementare la mappa grazie a Google Maps. 
STUDIO RICHIESTO:API Google

### Esportare il pdf con le transazioni 
Implementata funzionalità di export in formato PDF in base a delle date inserite in input.
Dopo le varie richieste di permesso per salvarlo, il pdf si struttura in due pagine divise per uscite ed entrate con tutte le informazioni che l’utente inserisce in input durante l’aggiunta di una transazione.
STUDIO RICHIESTO:
Gestione Permessi, Implementazione creazione e salvataggio file sul dispositivo
 

![screen1](https://user-images.githubusercontent.com/18367371/108081128-e70d7900-7070-11eb-8a38-9a5645ccf775.PNG)
![screen2](https://user-images.githubusercontent.com/18367371/108081132-e7a60f80-7070-11eb-902f-50cf734b2465.PNG)
![screen3](https://user-images.githubusercontent.com/18367371/108081136-e8d73c80-7070-11eb-83f8-a98a1210b9d7.PNG)
![screen4](https://user-images.githubusercontent.com/18367371/108081142-e96fd300-7070-11eb-9f22-7adece271526.PNG)
![screen5](https://user-images.githubusercontent.com/18367371/108081146-e96fd300-7070-11eb-8c09-78c81ff04614.PNG)
![screen6](https://user-images.githubusercontent.com/18367371/108081461-48cde300-7071-11eb-8fd9-78023c45e0e1.PNG)
![screen7](https://user-images.githubusercontent.com/18367371/108081467-49667980-7071-11eb-8a75-02b90fe116d6.PNG)
![screen8](https://user-images.githubusercontent.com/18367371/108081470-49ff1000-7071-11eb-9fec-8374dcf80889.PNG)
![screen9](https://user-images.githubusercontent.com/18367371/108081472-4a97a680-7071-11eb-9edb-d21316fa5047.PNG)
![screen10](https://user-images.githubusercontent.com/18367371/108081473-4b303d00-7071-11eb-88b6-edd5b29e89af.PNG)
