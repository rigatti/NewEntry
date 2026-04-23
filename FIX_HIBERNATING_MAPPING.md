# 🔧 FIX HIBERNATE MAPPING - Planning not mapped

## Problème
```
QuerySyntaxException: Planning is not mapped
```

La classe `Planning` n'est pas chargée dans le contexte Hibernate au démarrage.

## Cause probable
- WAR n'a pas été redéployé après modification
- Contexte Spring n'a pas été reinitialisé
- Fichier `planning.hbm.xml` n'a pas été inclus dans le classpath

## Vérifications effectuées ✅
- ✅ Fichier `planning.hbm.xml` existe dans `src/main/resources/db/entry/`
- ✅ Le mapping est correct et déclare `<class name="db.entry.Planning">`
- ✅ Le fichier est enregistré dans `hibernate.cfg.xml` ligne 29

## Solution : Forcer le rechargement du contexte

### Étape 1 : Arrêter Tomcat complètement
```powershell
# Arrêter complètement
net stop Tomcat9

# Vérifier qu'il n'y a plus de processus Java
Get-Process java -ErrorAction SilentlyContinue
# Si des processus restent, les tuer
Get-Process java | Stop-Process -Force
```

### Étape 2 : Nettoyer les répertoires de cache
```powershell
# Supprimer le répertoire work (cache Tomcat)
Remove-Item -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\work" -Recurse -Force -ErrorAction SilentlyContinue

# Supprimer le répertoire belex extrait
Remove-Item -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\webapps\belex" -Recurse -Force -ErrorAction SilentlyContinue

# Supprimer le ancien WAR
Remove-Item -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\webapps\belex.war" -Force -ErrorAction SilentlyContinue
```

### Étape 3 : Rebuild le WAR (important!)
```powershell
cd "C:\Users\miche\Downloads\temp belex\belexGit\NewEntry"
mvn clean package -DskipTests
```

### Étape 4 : Redéployer le WAR
```powershell
Copy-Item -Path "target\belex.war" -Destination "C:\Users\miche\Downloads\apache-tomcat-9.0.115\webapps\" -Force
```

### Étape 5 : Redémarrer Tomcat
```powershell
net start Tomcat9
```

### Étape 6 : Vérifier les logs
```powershell
# Attendre 10 secondes pour le démarrage
Start-Sleep -Seconds 10

# Lire les logs
Get-Content -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\BelexStockLog.log" -Wait
```

Vous devriez voir dans les logs :
```
[INFO] Hibernate: Planning is now loaded
[DEBUG] org.belex.arrival.ArrivalBusinessImpl - getPlannedSuppliers called
```

## 🎯 Vérification finale

Après redémarrage, l'erreur `Planning is not mapped` ne devrait plus apparaître.

Si elle persiste :
1. Vérifier que le fichier `target/belex.war` contient bien `planning.hbm.xml`
   ```bash
   jar tf target/belex.war | grep planning
   # Devrait afficher : WEB-INF/classes/db/entry/planning.hbm.xml
   ```

2. Vérifier que `hibernate.cfg.xml` est bien dans le WAR
   ```bash
   jar tf target/belex.war | grep hibernate.cfg.xml
   # Devrait afficher : WEB-INF/classes/hibernate.cfg.xml
   ```

3. Consulter `catalina.log` pour les erreurs Spring Context :
   ```
   C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\catalina.log
   ```

