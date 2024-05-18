export interface Threshold {
  id: number;
  value: number;
  roomId: number;
  sensorType: SensorType;
  thresholdType: ThresholdType;
  tip : ThresholdTip;
}

export enum SensorType {
  TEMPERATURE = 'TEMPERATURE',
  IRRADIANCE = 'IRRADIANCE',
  HUMIDITY = 'HUMIDITY',
  NMVOC = 'NMVOC'
}

export enum ThresholdType {
  UPPERBOUND_INFO = 'UPPERBOUND_INFO',
  LOWERBOUND_INFO = 'LOWERBOUND_INFO',
  UPPERBOUND_WARNING = 'UPPERBOUND_WARNING',
  LOWERBOUND_WARNING = 'LOWERBOUND_WARNING'
}

interface ThresholdTip {
  id: number;
  tip: string;
}

export interface ThresholdUpdateDto {
  threshold: Threshold;
  reason: string;
}
export interface ThresholdTipUpdateDto {
  id: number;
  tip: string;
}
