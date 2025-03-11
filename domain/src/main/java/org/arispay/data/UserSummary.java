package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummary {
    private Long totalUsers;
    private Long activeUsers;
    private Long pendingUsers;
    private Long inactiveUsers;
}
