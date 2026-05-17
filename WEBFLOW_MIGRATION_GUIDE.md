# Spring WebFlow Migration Guide - Java 21 + WebFlow 2.5.1

## Quick Concepts

### WebFlow 1 → WebFlow 2.5.1 Syntax Changes

| Feature | WebFlow 1 | WebFlow 2.5.1 |
|---------|----------|--------|
| **Start state** | `<start-state idref="...">` | `start-state="..."` attribute on `<flow>` |
| **Action call** | `<bean-action bean="name" method="..."><method-arguments>` | `<evaluate expression="name.method(...)">` |
| **View binding** | Manual in action or JSP | `model="beanName"` on `<view-state>` |
| **Event trigger** | `<action bean="..." method="...">` | `<evaluate expression="...">` inside transitions |
| **Transitions** | `on="eventName"` + global `<transition>` | `on="eventName"` inside `<view-state>` |
| **Form action** | Separate `<action>` elements | Single `<evaluate>` or automatic binding |

---

## productEntry.xml Flow Logic

### 3-Step Process:

1. **Date Selection** → Show planned suppliers for a given date
   - User picks date → Call `getPlannedSuppliers(arrival)` → Display suppliers list
   
2. **Supplier Selection** → Check if supplier has pending entries
   - User picks supplier → Call `checkPending(arrival)` → Show supplier list OR warning
   
3. **Product Entry** → Start recording products for selected supplier
   - Call `getPending(arrival)` to detect existing unfinished entries
   - Show form/warning based on result

---

## State Structure

```
START: searchPlannedSuppliersAct
  │
  ├─ getPlannedSuppliers() → fills arrival.suppliers
  │
  ↓
selectArrivalDateView (JSP: selectArrivalDate.jsp)
  │
  ├─ Form 1: Search date change
  │   Event: searchPlannedSupplier
  │   Action: None (field already in Arrival model)
  │   Target: searchPlannedSuppliersAct (re-fetch suppliers)
  │
  └─ Form 2: Show suppliers
      Event: showSuppliers
      Action: None (field already in Arrival model)
      Target: checkPendingArrivalAct
      │
      ↓
      checkPendingArrivalAct (Action)
        │ checkPending() → sets supplier.status
        │
        ├─ success → showSuppliersView
        └─ error → fatalErrorView
          │
          ↓
          showSuppliersView (JSP: showSuppliers.jsp)
            │
            └─ Form: Select supplier
                Event: searchEntry
                Binding: supplier.supplierCode (from <select>)
                Target: fillPlannedSupplierAct
                │
                ↓
                fillPlannedSupplierAct (Action)
                  │ getPlannedSupplier() → fills Supplier details
                  │
                  ├─ isClosed → closedListView
                  └─ isNotClosed → getPendingArrivalAct
                    │
                    ├─ true (pending exists) → warningPendingDetectedView
                    └─ false → supplierDocumentFormView
```

---

## Key WebFlow 2.5.1 Patterns Used in productEntry.xml

### 1. Model Binding on View-States
```xml
<view-state id="selectArrivalDateView" view="productEntry/selectArrivalDate" model="arrival">
```
- Spring automatically binds form fields to `arrival` POJO
- No need for explicit `bindAndValidate()` calls
- Form fields with names like `supplier.supplierCode` automatically map to nested properties

### 2. Transitions Without Explicit Actions (when form fields are already bound)
```xml
<transition on="searchPlannedSupplier" to="searchPlannedSuppliersAct" />
```
- No `<evaluate>` needed if field binding is automatic

### 3. Action State Returning Events
```xml
<action-state id="checkPendingArrivalAct">
    <evaluate expression="arrivalBusiness.checkPending(flowScope.arrival)" />
    <transition on="success" to="showSuppliersView" />
    <transition on="error" to="fatalErrorView" />
</action-state>
```
- Business method returns `Event` (Spring detects event name from Event.id)

### 4. Form Submission URLs
```jsp
<!-- Correct for WebFlow 2.5.1 -->
<form action="${flowExecutionUrl}" method="post">
    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
    <input type="submit" name="_eventId_eventName" value="Button Label" />
</form>
```

---

## Common Mistakes to Avoid

❌ **WRONG**: Using old WebFlow 1 syntax
```xml
<bean-action bean="arrivalBusiness" method="checkPending">
    <method-arguments><argument expression="flowScope.arrival"/></method-arguments>
</bean-action>
```

✅ **CORRECT**: WebFlow 2.5.1 syntax
```xml
<evaluate expression="arrivalBusiness.checkPending(flowScope.arrival)" />
```

---

❌ **WRONG**: Not binding data to model
```xml
<view-state id="selectArrivalDateView" view="productEntry/selectArrivalDate">
    <!-- Manual binding action needed -->
</view-state>
```

✅ **CORRECT**: Use model attribute
```xml
<view-state id="selectArrivalDateView" view="productEntry/selectArrivalDate" model="arrival">
    <!-- Automatic binding -->
</view-state>
```

---

❌ **WRONG**: Using iframe for flow navigation
```jsp
<iframe src="/belex/flow/arrival?_flowExecutionKey=...&_eventId_..."></iframe>
```

✅ **CORRECT**: Use POST form with flowExecutionUrl
```jsp
<form action="${flowExecutionUrl}" method="post">
    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
    <input type="submit" name="_eventId_eventName" value="Next" />
</form>
```

---

## Testing the Migration

After updating XML and JSPs:

1. **Set log4j2 DEBUG** for `org.springframework.webflow`:
   ```properties
   log4j.appender.stdout.ConversionPattern=%d [%t] %-5p %-50c - %m%n
   ```

2. **Test Date Selection**:
   - Load `/belex/flow/productEntry`
   - Pick a date with planned suppliers
   - Verify suppliers list appears
   - Change date, verify list updates

3. **Test Supplier Selection**:
   - Click "Show Suppliers" button
   - Select a supplier
   - Verify flow navigates to next step

4. **Test Pending Detection**:
   - Check logs for `checkPending()` execution
   - Verify correct warning/form page appears

---

## Files Modified

- `src/main/webApp/WEB-INF/flows/v2-productEntry.xml` --- Main migration
- `src/main/webApp/WEB-INF/jsp/productEntry/selectArrivalDate.jsp` ← Form updates
- `src/main/webApp/WEB-INF/jsp/productEntry/showSuppliers.jsp` ← Form updates  
- `src/main/webApp/WEB-INF/jsp/productEntry/warningPendingDetected.jsp` ← Form updates

