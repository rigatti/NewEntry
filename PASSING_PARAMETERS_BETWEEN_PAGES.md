# Passing Parameters Between Pages in Spring WebFlow 2.5.1

## Overview

Passing parameters (request data) from one page to another in Spring WebFlow 2.5.1 requires understanding the difference between WebFlow 1.0 and 2.5.1. This document outlines the solution implemented in the `arrival` flow for handling the `searchSupplierDate` parameter.

## Problem

In WebFlow 1.0, parameter binding was automatic via `<binder>` elements. In WebFlow 2.5.1, this requires explicit handling.

**Scenario:** User selects a date on page A, and we need to pass it to page B for processing.

## Solution

### 1. Flow Configuration (v2-productEntry.xml)

#### At Flow Start
```xml
<!-- Bind search supplier date from request parameters -->
<action-state id="bindSearchSupplierDateAct">
    <evaluate expression="flowScope.arrival.setSearchSupplierDate(externalContext.getRequestParameterMap().get('searchSupplierDate'))" />
    <transition to="searchPlannedSuppliersAct" />
</action-state>
```

**Key Points:**
- `externalContext.getRequestParameterMap().get('searchSupplierDate')` extracts the POST parameter
- `flowScope.arrival.setSearchSupplierDate(...)` directly calls the setter on the form object
- This action-state must be the `start-state` to capture parameters on initial request

#### On View Transitions
```xml
<transition on="searchPlannedSupplier" to="searchPlannedSuppliersAct">
    <evaluate expression="arrivalFormAction.bindSearchSupplierDate(flowRequestContext)" />
</transition>
```

**Key Points:**
- Use the `ArrivalFormAction.bindSearchSupplierDate()` method (bean is accessible in transitions)
- This method extracts the parameter from the current request

### 2. Form Action Class (ArrivalFormAction.java)

```java
@Slf4j
public class ArrivalFormAction extends FormAction {
    
    public Event bindSearchSupplierDate(RequestContext context) throws Exception {
        Arrival arrival = (Arrival) getFormObject(context);
        String searchSupplierDate = context.getExternalContext()
            .getRequestParameterMap().get("searchSupplierDate");
        
        if (searchSupplierDate != null && !searchSupplierDate.isEmpty()) {
            arrival.setSearchSupplierDate(searchSupplierDate);
        }
        
        return success();
    }
}
```

**Why this approach?**
- Beans are NOT accessible in action-state SpEL expressions directly
- Must be accessed through proper Spring context
- Returns `Event` for flow transition handling

### 3. HTML Form (selectArrivalDate.jsp)

```html
<form name="searchPlannedSupplierFrm" action="${pageContext.request.contextPath}/flow/arrival" method="post">
    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
    <input type="hidden" name="_eventId_searchPlannedSupplier" value="">
    
    <!-- This parameter is extracted in the flow -->
    <input name="searchSupplierDate" id="searchSupplierDate" type="hidden" 
           value="<%= Util.formatDate(arrival.getSearchSupplierDate(), "yyyyMMdd", "dd/MM/yyyy") %>">
</form>
```

**Key Points:**
- Parameter name MUST match the one used in flow (`searchSupplierDate`)
- Always include `_flowExecutionKey` for flow context
- Include `_eventId_*` to trigger specific transitions

## Common Pitfalls

### ❌ Mistake 1: Accessing beans in action-state start
```xml
<!-- This FAILS -->
<action-state id="bindSearchSupplierDateAct">
    <evaluate expression="arrivalFormAction.bindSearchSupplierDate(flowRequestContext)" />
</action-state>
```
**Error:** `Property or field 'arrivalFormAction' cannot be found`

**Fix:** Use direct SpEL access or method in FormAction subclass

### ❌ Mistake 2: Using requestParameters instead of externalContext
```xml
<!-- This FAILS -->
<evaluate expression="flowScope.arrival.setSearchSupplierDate(requestParameters.searchSupplierDate)" />
```
**Error:** `requestParameters` doesn't exist in WebFlow 2.5.1

**Fix:** Use `externalContext.getRequestParameterMap().get('paramName')`

### ❌ Mistake 3: Not binding on initial request
```xml
<!-- This MISSES the parameter on first request -->
<action-state id="searchPlannedSuppliersAct" start-state="true">
    <evaluate expression="arrivalBusiness.getPlannedSuppliers(...)" />
</action-state>
```

**Fix:** Add binding action-state as the `start-state`

## Best Practice Pattern

```xml
<flow start-state="bindParametersAct">
    <on-start>
        <set name="flowScope.myObject" value="new com.example.MyObject()" />
    </on-start>

    <!-- Step 1: Bind incoming parameters -->
    <action-state id="bindParametersAct">
        <evaluate expression="flowScope.myObject.setParam1(externalContext.getRequestParameterMap().get('param1'))" />
        <evaluate expression="flowScope.myObject.setParam2(externalContext.getRequestParameterMap().get('param2'))" />
        <transition to="mainActionAct" />
    </action-state>

    <!-- Step 2: Process with bound parameters -->
    <action-state id="mainActionAct">
        <evaluate expression="myBusiness.process(flowScope.myObject)" result="flowScope.myObject" />
        <transition to="viewState" />
    </action-state>

    <!-- Step 3: View with option to re-send parameters -->
    <view-state id="viewState" view="myView">
        <transition on="submit" to="mainActionAct">
            <evaluate expression="myFormAction.bindParameters(flowRequestContext)" />
        </transition>
    </view-state>
</flow>
```

## Summary

| Task | Method | Location |
|------|--------|----------|
| Bind initial parameters | SpEL: `externalContext.getRequestParameterMap().get()` | Start action-state |
| Bind on transitions | Method in FormAction subclass | View-state transitions |
| Access from view | Direct getter on flowScope object | JSP expressions |
| Pass to next request | Hidden input with same parameter name | HTML form |

## Implementation Checklist

- [ ] Create `start-state` action-state for binding initial parameters
- [ ] Use `externalContext.getRequestParameterMap().get('paramName')` in SpEL
- [ ] Create FormAction method for binding in transitions
- [ ] Add `@Slf4j` to FormAction for logging if needed
- [ ] Include `_flowExecutionKey` in all forms
- [ ] Include `_eventId_*` to specify transitions
- [ ] Use hidden inputs for parameter passing
- [ ] Test with initial request (first time user enters flow)
- [ ] Test with transitions (subsequent parameter changes)

