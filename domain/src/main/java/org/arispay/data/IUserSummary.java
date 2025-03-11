package org.arispay.data;

public interface IUserSummary {
     Long getTotalUsers();
     Long getActiveUsers();
     Long getPendingUsers();
     Long getInactiveUsers();
}
