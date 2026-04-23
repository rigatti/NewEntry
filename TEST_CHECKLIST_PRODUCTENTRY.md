# 🧪 CHECKLIST TESTS - Migration productEntry.xml

## Pré-tests (Infrastructure)

- [ ] WAR déployé sur le serveur d'application
- [ ] URL accessible : http://localhost:8080/belex/flow/arrival
- [ ] Logs visibles dans `/WEB-INF/jsptmp/` ou console
- [ ] Database connectée et fonctionnelle
- [ ] Hibernate show_sql=true pour debug (optionnel)

---

## Test 1 : Initialisation du flux

### Action
1. Accéder à `/flow/arrival`
2. Observer le `<on-start>` qui crée une nouvelle instance `Arrival`

### Attentes
- ✅ Page de sélection de date d'arrivée s'affiche
- ✅ Pas d'erreur dans les logs Spring WebFlow
- ✅ Variable `flowScope.arrival` initialisée

### Commandes debug
```
log.debug("flowScope.arrival initialized: " + flowScope.arrival)
```

---

## Test 2 : Action searchPlannedSuppliersAct

### Action
1. Page s'affiche → exécute `arrivalBusiness.getPlannedSuppliers(flowScope.arrival)`
2. Retour vers `selectArrivalDateView`

### Attentes
- ✅ Transition vers selectArrivalDateView effectuée
- ✅ Liste des fournisseurs planifiés disponible
- ✅ Pas d'erreur SpEL dans les logs

### Points à vérifier
```java
// Vérifier que getPlannedSuppliers() retourne une Arrival modifiée
Arrival result = arrivalBusiness.getPlannedSuppliers(flowScope.arrival);
assert result != null : "getPlannedSuppliers() ne doit pas retourner null";
```

---

## Test 3 : Vue selectArrivalDateView

### Action - Bouton "searchPlannedSupplier"
1. Saisir/modifier une date
2. Cliquer sur "searchPlannedSupplier"
3. Exécute : `arrivalFormAction.bindAndValidate(flowRequestContext)`

### Attentes
- ✅ Formulaire validé sans erreurs
- ✅ Retour vers searchPlannedSuppliersAct (boucle de recherche)
- ✅ Nouveaux résultats affichés

### Action - Bouton "showSuppliers"
1. Cliquer sur "showSuppliers"
2. Exécute : `arrivalFormAction.bindAndValidate(flowRequestContext)`
3. Transition vers checkPendingArrivalAct

### Attentes
- ✅ Validation passée
- ✅ Pas d'erreurs de binding
- ✅ Page de confirmation d'arrivée affichée

---

## Test 4 : Transitions avec bindAndValidate()

### Cas critique ⚠️
La méthode `bindAndValidate(flowRequestContext)` DOIT :
1. Accepter un `RequestContext` en paramètre
2. Valider les données du formulaire
3. Binder les valeurs à flowScope

### Code à vérifier
```java
public void bindAndValidate(RequestContext context) {
    // Récupérer flowScope si nécessaire
    MutableAttributeMap flowScope = context.getFlowScope();
    
    // Valider et binder
    // ...
}
```

### Attentes
- ✅ Pas de `NullPointerException` sur `flowRequestContext`
- ✅ Les valeurs saisies sont correctement liées à `flowScope`
- ✅ Les validations s'exécutent correctement

---

## Test 5 : Actions retournant des booléens

### Exemple : checkPendingArrivalAct
```xml
<action-state id="getPendingArrivalAct">
    <evaluate expression="arrivalBusiness.getPending(flowScope.arrival)"/>
    <transition on="true" to="warningPendingDetectedView" />
    <transition on="false" to="supplierDocumentFormView" />
</action-state>
```

### Action
1. Créer une situation avec une arrivée en attente
2. Naviguer jusqu'à getPendingArrivalAct

### Attentes
- ✅ Si pending = true → affichage warningPendingDetectedView
- ✅ Si pending = false → affichage supplierDocumentFormView
- ✅ La méthode `getPending()` retourne un booléen évaluable

### Code à vérifier
```java
public boolean getPending(Arrival arrival) {
    // Doit retourner true ou false
    // WebFlow évalue le résultat pour choisir la transition
    return arrival.hasPendingEntries();
}
```

---

## Test 6 : Flux complet productSelection → completeEntryForm

### Action
1. Naviguer jusqu'à entryFormView
2. Chercher un produit → selectionnez "productSelection"
3. Exécute : `arrivalBusiness.productSelection(flowScope.arrival, flowScope.products)`
4. Transition vers selectBasketAct

### Attentes
- ✅ Produits chargés dans `flowScope.products`
- ✅ `productSelection()` retourne une Arrival modifiée
- ✅ Transition vers selectBasketAct effectuée
- ✅ Vue completeEntryForm s'affiche

### Points critiques
- `flowScope.products` doit être populée avant transition
- La méthode productSelection() doit initialiser flowScope.products

---

## Test 7 : Validation complexe avec validator

### Exemple : confirmEntryAct
```xml
<action-state id="confirmEntryAct">
    <evaluate expression="arrivalFormAction.bindAndValidate(flowRequestContext)"/>
    <transition on="success" to="saveTempEntryAct" />
    <transition on="error" to="completeEntryFormView" />
</action-state>
```

### Action
1. Remplir formulaire d'entrée
2. Cliquer "Confirmer"
3. Exécute validation avec attribut `validatorMethod` (si applicable)

### Attentes (code original WebFlow 1)
- ✅ Si validation OK → saveTempEntryAct
- ✅ Si validation échoue → redirection completeEntryFormView
- ✅ Messages d'erreur affichés

### NOTE
Le code original WebFlow 1 utilisait :
```xml
<attribute name="validatorMethod" value="validateCompleteEntry" />
```
En WebFlow 2.5.1, utiliser `<evaluate>` + exception handling ou état intermédiaire.

---

## Test 8 : Méthodes potentiellement problématiques

### ❌ À tester en priorité

#### 1. arrivalFormAction.saveTempEntry()
```java
// WebFlow 1 pattern
public void saveTempEntry() { /* sans paramètres */ }

// WebFlow 2.5.1 peut nécessiter :
public void saveTempEntry(RequestContext context) {
    MutableAttributeMap flowScope = context.getFlowScope();
    Arrival arrival = (Arrival) flowScope.get("arrival");
}
```

**Test** : Naviguer jusqu'à saveTempEntryAct, vérifier logs
- [ ] Pas d'erreur de signature de méthode
- [ ] Les données sont correctement sauvegardées en DB

#### 2. arrivalFormAction.updateTempEntry()
Même logique que saveTempEntry()
- [ ] Pas d'erreur de signature
- [ ] Mise à jour correcte en DB

#### 3. arrivalFormAction.resetNewEntry()
```java
// Similaire
public void resetNewEntry(RequestContext context) { /* ... */ }
```

**Test** : Après confirmation d'entrée, vérifier que resetNewEntry() fonctionne
- [ ] flowScope.arrival réinitialisée
- [ ] Formulaire vide pour nouvelle entrée

#### 4. arrivalFormAction.checkProductsFound()
```xml
<evaluate expression="arrivalFormAction.checkProductsFound(flowRequestContext)"/>
```

**Test** : Recherche de produits
- [ ] Pas de NullPointerException sur flowRequestContext
- [ ] Transitions on="single", on="multiple", on="none" fonctionnent

---

## Test 9 : Flux fermé (Closed Entries)

### Action
1. Créer une entrée fermée/locked
2. Naviguer jusqu'à fillPlannedSupplierAct
3. Observer transition sur "isClosed" vers closedListView

### Attentes
- ✅ closedListView s'affiche
- ✅ closedCompleteProductsAct charge les produits fermés
- ✅ closedSelectBasketAct et closedBasketDetailView fonctionnent
- ✅ Impossibilité de modifier une entrée fermée (verrouillée)

---

## Test 10 : Gestion des erreurs

### Cas 1 : Erreur dans storeEntryAct
```xml
<action-state id="storeEntryAct">
    <evaluate expression="arrivalBusiness.storeEntry(flowScope.arrival)" result="flowScope.result"/>
    <transition on="error" to="fatalErrorView" />
</action-state>
```

**Test** : Créer une situation qui provoque une erreur de stockage
- [ ] Transition vers fatalErrorView effectuée
- [ ] Message d'erreur affiché
- [ ] Logs contiennent le stacktrace

### Cas 2 : Warning dans warningEntryView
```xml
<view-state id="warningEntryView" view="productEntry/warningEntry">
    <transition on="confirm" to="forceStoreEntryAct" />
    <transition on="cancel" to="entryFormView" />
</view-state>
```

**Test** : Créer une situation de warning (ex: doublon)
- [ ] Vue d'avertissement affichée
- [ ] Bouton "Confirmer" force le stockage
- [ ] Bouton "Annuler" retour au formulaire

---

## Test 11 : Modification d'entrées confirmées

### Action
1. Afficher listOfConfirmedEntryView
2. Cliquer "Modifier"
3. Transition vers modifyEntryView avec requestParamsFormAction.bindAndValidate()

### Attentes
- ✅ Formulaire de modification s'affiche
- ✅ Données de l'entrée chargées
- ✅ Modification validée avec confirmModifyEntryAct
- ✅ Retour à listOfConfirmedEntryView après confirmation

### Points à vérifier
```java
// modifyEntryView utilise requestParamsFormAction
public void bindAndValidate(RequestContext context) {
    MutableAttributeMap flowScope = context.getFlowScope();
    RequestParams params = (RequestParams) flowScope.get("requestParams");
    // ...
}
```

---

## Test 12 : Suppression d'entrées

### Action
1. Afficher listOfConfirmedEntryView
2. Cliquer "Supprimer"
3. Exécute removeEntryAct

### Attentes
- ✅ Entrée supprimée de la DB
- ✅ Transition on="noMoreEntry" si c'était la dernière
- ✅ Transition on="existEntry" si d'autres entrées existent
- ✅ État cohérent après suppression

---

## Test 13 : Nouveau produit pendant l'entrée

### Action
1. Depuis entryFormView, cliquer "Nouveau produit"
2. Transition vers newProductFormView
3. Remplir et soumettre
4. Exécute newProductAct → arrivalBusiness.newProduct()

### Attentes
- ✅ newProductFormView s'affiche
- ✅ newProductAct insère le produit en DB
- ✅ Transition vers completeProductsAct
- ✅ Nouveau produit disponible dans la recherche

---

## Test 14 : Mise à jour unité de produit

### Action
1. Depuis completeEntryFormView
2. Cocher "updateProductUnitAndSubmit"
3. Exécute updateProductUnitAct → productBusiness.updateProductUnit()

### Attentes
- ✅ Unité mise à jour pour le produit
- ✅ Transition vers confirmEntryAct
- ✅ Entrée sauvegardée correctement

---

## Test 15 : Remplissage unité sans sauvegarde

### Action
1. Depuis completeEntryFormView
2. Cocher "submitDontSaveUnit"
3. Exécute fillSelectedUnitAct → productBusiness.fillSelectedUnit()

### Attentes
- ✅ Unité remplie mais pas sauvegardée
- ✅ Transition vers confirmEntryAct
- ✅ Entrée confirmée sans modification de produit

---

## Points de vérification générale

### Logs Spring WebFlow
```
DEBUG org.springframework.webflow - Entering state: [selectArrivalDateView]
DEBUG org.springframework.webflow - Execution of [evaluateable] resulted in [...]
DEBUG org.springframework.webflow - Handling transition [searchPlannedSupplier]
```

- [ ] Pas de `NullPointerException`
- [ ] Pas de `ClassCastException`
- [ ] Pas de `org.springframework.binding.expression.ExpressionException`

### Validation des transitions
```xml
<transition on="searchPlannedSupplier" to="searchPlannedSuppliersAct">
```

- [ ] L'ID de destination existe bien : `searchPlannedSuppliersAct`
- [ ] L'événement "searchPlannedSupplier" est déclenché par le formulaire JSP

### FlowScope et session
- [ ] `flowScope.arrival` n'est jamais null (sauf intentionnellement)
- [ ] `flowScope.products` initialisée avant utilisation
- [ ] `flowScope.requestParams` initialisée avant modifyEntryView

---

## Rapport de test

### Template à remplir après tests
```
Date : __________
Testeur : __________
Environnement : __________ (local/dev/staging)

Tests réussis   : __ / 15
Tests échoués   : __ / 15
Tests bloquants : __ / 15

Bugs trouvés :
[ ] Aucun
[ ] Signature de méthode manquante
[ ] Erreur de transition
[ ] Erreur de validation
[ ] Autre : _______________

Notes :
_________________________________
_________________________________
```

---

## Arrêt des tests

**Critères de succès** :
- ✅ Tous les tests passent
- ✅ Aucun avertissement Spring WebFlow
- ✅ Flux complet productEntry fonctionnel
- ✅ Pas de régressions vs. version WebFlow 1.0

**Conditions de migration des autres fichiers** :
- ✅ productEntry.xml 100% opérationnel
- ✅ Aucun bug critique trouvé

