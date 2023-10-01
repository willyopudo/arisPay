<%@ include file="common/header.jsp"%>
<%@ include file="common/aside.jsp"%>
<%--<%@ include file="common/navigation.jsp"%>--%>
<!-- Layout container -->
<div class="layout-page">
    <%@ include file="common/navigation.jsp"%>
    <div class="container-xxl flex-grow-1 container-p-y">

        <div class="col-xl">
          <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
               <spring:url value="/machine/addMachine" var="addURL" />
              <h5 class="mb-0">Add Machine</h5>
              <small class="text-muted float-end">New Machine</small>
            </div>
            <div class="card-body">
              <form:form modelAttribute="machineForm" method="post" action="${addURL}" >
                <form:hidden path="id"/>
                <div class="mb-3">
                  <label class="form-label" for="basic-default-machinecode">Machine Code</label>
                  <form:input type="text" path="machinecode" class="form-control" id="basic-default-machinecode" placeholder="M001" />
                </div>
                <div class="mb-3">
                  <label class="form-label" for="basic-default-machinerent">Machine Rent</label>
                  <form:input type="text" path="machinerent" class="form-control" id="basic-default-machinerent" placeholder="1200" />
                </div>
                <div class="mb-3">
                  <label class="form-label" for="basic-default-machinelatitude">Latitude</label>
                  <form:input type="text" path="machinelatitude" class="form-control" id="basic-default-machinelatitude" placeholder="1.0078644" />
                </div>
                <div class="mb-3">
                  <label class="form-label" for="basic-default-machinerent">Longitude</label>
                  <form:input type="text" path="machinelongitude" class="form-control" id="basic-default-machinelongitude" placeholder="36.907529" />
                </div>

                <button type="submit" class="btn btn-primary">Submit</button>
              </form:form>
            </div>
          </div>
        </div>
    </div>

 <%@ include file="common/footer.jsp"%>
