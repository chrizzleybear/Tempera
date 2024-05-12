/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { ProjectDto } from './projectDto';
import { ColleagueStateDto } from './colleagueStateDto';


export interface DashboardDataResponse { 
    temperature: number;
    humidity: number;
    irradiance: number;
    nmvoc: number;
    visibility: DashboardDataResponse.VisibilityEnum;
    state?: DashboardDataResponse.StateEnum;
    stateTimestamp?: string;
    defaultProject?: ProjectDto;
    availableProjects?: Array<ProjectDto>;
    colleagueStates?: Array<ColleagueStateDto>;
}
export namespace DashboardDataResponse {
    export type VisibilityEnum = 'PUBLIC' | 'PRIVATE' | 'HIDDEN';
    export const VisibilityEnum = {
        Public: 'PUBLIC' as VisibilityEnum,
        Private: 'PRIVATE' as VisibilityEnum,
        Hidden: 'HIDDEN' as VisibilityEnum
    };
    export type StateEnum = 'AVAILABLE' | 'MEETING' | 'OUT_OF_OFFICE' | 'DEEPWORK';
    export const StateEnum = {
        Available: 'AVAILABLE' as StateEnum,
        Meeting: 'MEETING' as StateEnum,
        OutOfOffice: 'OUT_OF_OFFICE' as StateEnum,
        Deepwork: 'DEEPWORK' as StateEnum
    };
}


