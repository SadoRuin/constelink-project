
export interface DonationData {
  beneficiaryId:number;
  beneficiaryBirthday: number;
  categoryName:string;
  beneficiaryStatus:string;
  fundraisingAmountGoal: number;
  fundraisingAmountRaised: number;
  fundraisingEndTime:number;
  fundraisingIsDone: boolean
  fundraisingPeople:number
  fundraisingStartTime: number;
  fundraisingStory: string;
  fundraisingThumbnail: string;
  fundraisingTitle: string;
  fundraisingId: number;
  photo: string;
}


export interface Statistics {
  totalFundraisings: number,
  totalFundraisingsFinished: number,
  totalAmountedCash: number,
  allDonation: number,
  allMember: number,
  allHospital: number,
}
