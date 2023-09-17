<%@ include file="common/header.jsp"%>
<%@ include file="common/aside.jsp"%>
<%--<%@ include file="common/navigation.jsp"%>--%>
<!-- Layout container -->
<div class="layout-page">
    <%@ include file="common/navigation.jsp"%>
    <div class="container-xxl flex-grow-1 container-p-y">
        <div class="row">
            <div class="col-lg-12 mb-4 order-0">
              <div class="card">
                  <div class="col-lg-12">
                      <h5 class="card-header float-start">List of Machines</h5>
                      <a class="btn btn-primary float-end m-3" href="/machine/addMachine/">Create New</a>
                  </div>
                  <div class="table-responsive text-nowrap">
                    <table class="table">
                      <thead class="table-dark">
                        <tr>
                          <th scope="row">Machine Code</th>
                          <th scope="row">Machine Rent</th>
                          <th scope="row">Latitude</th>
                          <th scope="row">Longitude</th>
                          <th scope="row">Milk Temperature</th>
                          <th scope="row">Milk Volume</th>
                          <th scope="row">Oil Volume</th>
                          <th scope="row">Created Date</th>
                          <th scope="row">Status</th>
                          <th scope="row">Actions</th>

                        </tr>
                      </thead>
                      <tbody class="table-border-bottom-0">
                        <c:forEach items="${machineList}" var="machine" >
                            <tr>
                                <td>${machine.machinecode}</td>
                                <td>${machine.machinerent}</td>
                                <td>${machine.machinelatitude}</td>
                                <td>${machine.machinelongitude}</td>
                                <td>${machine.milktemp}</td>
                                <td>${machine.milkvolume}</td>
                                <td>${machine.oilvolume}</td>
                                <td>${machine.createddatetime}</td>
                                <td><span class="badge bg-label-primary me-1">Active</span></td>
                                <td>
                                    <div class="dropdown">
                                      <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                        <i class="bx bx-dots-vertical-rounded"></i>
                                      </button>
                                      <div class="dropdown-menu">
                                        <spring:url value="/machine/editMachine/${machine.id}" var="editURL" />
                                        <a class="dropdown-item" href="${editURL}"
                                          ><i class="bx bx-edit-alt me-1"></i> Edit</a
                                        >
                                        <spring:url value="/machine/deleteMachine/${machine.id}" var="deleteURL" />
                                        <a class="dropdown-item" href="${deleteURL}"
                                          ><i class="bx bx-trash me-1"></i> Delete</a
                                        >
                                      </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </div>
              </div>

            </div>
        </div>
    </div>

<%@ include file="common/footer.jsp"%>