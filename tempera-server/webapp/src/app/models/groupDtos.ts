export interface GroupCreateDTO {
  name: string;
  description: string;
  groupLead: string;
}

export interface GroupUpdateDTO {
  id: number;
  name: string;
  description: string;
  groupLead: string;
}

export interface GroupMemberDTO {
  groupId: number;
  memberId: string;
}
