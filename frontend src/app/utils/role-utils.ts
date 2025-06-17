// src/app/utils/role-utils.ts
export function getRoleName(roleId: number): string {
  switch(roleId) {
    case 1: return 'CUSTOMER';
    case 2: return 'BANK_EMPLOYEE';
    case 3: return 'ADMINISTRATOR';
    default: return 'UNKNOWN';
  }
}
