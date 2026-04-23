# 🔧 FIX LOGS - Étapes pour activer les logs

## Problème
Les logs n'apparaissaient pas car :
1. Chemin absolu du fichier log4j.xml était hardcodé
2. Le répertoire `/logs` de Tomcat pouvait ne pas exister
3. Les appenders n'étaient pas bien configurés

## Solution appliquée

**log4j.xml a été mis à jour :**

### Changement 1 - Chemin des logs
```xml
AVANT :
<param name="File" value="C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\BelexStockLog.log" />

APRÈS :
<param name="File" value="${catalina.base}/logs/BelexStockLog.log" />
```

Cette variable `${catalina.base}` sera automatiquement remplacée par le chemin Tomcat.

### Changement 2 - Appender Console amélioré
```xml
AVANT :
<param name="ConversionPattern" value="[%t] %p %c - %m%n" />

APRÈS :
<param name="Target" value="System.out"/>
<param name="ConversionPattern" value="[%-5p] %c{1} - %m%n" />
```

## ⚙️ Étapes pour activer les logs

### 1. Rebuild le WAR avec le nouveau log4j.xml
```powershell
cd "C:\Users\miche\Downloads\temp belex\belexGit\NewEntry"
mvn clean package -DskipTests
```

### 2. Créer le répertoire logs Tomcat (si nécessaire)
```powershell
New-Item -ItemType Directory -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs" -Force
```

### 3. Redéployer le WAR
```powershell
Copy-Item -Path "target\belex.war" -Destination "C:\apache-tomcat\webapps\" -Force
```

### 4. Arrêter et redémarrer Tomcat
```powershell
net stop Tomcat9
net start Tomcat9
```

### 5. Vérifier les logs

**Option A - Console (Tomcat window):**
```
Vous verrez les logs dans la fenêtre de console Tomcat
[INFO] ... 
[DEBUG] ...
```

**Option B - Fichier log:**
```powershell
Get-Content -Path "C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\BelexStockLog.log" -Wait
# Ou
tail -f "C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs\BelexStockLog.log"
```

## 📋 Configuration du logging

Les logs actuellement configurés pour DEBUG :
- ✅ `org.springframework.webflow` - Pour débuguer le flux WebFlow
- ✅ `org.springframework.binding` - Pour débugger les bindings
- ✅ `db.*` - Pour débugger les DAOs
- ✅ `org.belex.*` - Pour débugger votre business logic

## 🎯 Vérification

Après redémarrage, vous devriez voir dans les logs :
```
[DEBUG] org.springframework.webflow.engine.impl.FlowExecutionImpl - Starting flow execution
[DEBUG] org.springframework.webflow.engine.impl.FlowImpl - Starting flow 'arrival'
[DEBUG] org.belex.arrival.ArrivalBusinessImpl - getPlannedSuppliers called
```

Si rien n'apparaît, vérifiez :
1. Le répertoire existe : `C:\Users\miche\Downloads\apache-tomcat-9.0.115\logs`
2. Les permissions du répertoire sont correctes
3. Le WAR a été redéployé (contient le nouveau log4j.xml)
4. Tomcat a été redémarré

