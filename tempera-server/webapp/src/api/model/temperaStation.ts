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
import { AccessPoint } from './accessPoint';
import { Userx } from './userx';
import { Sensor } from '../api/sensor';


export interface TemperaStation {
    id?: string;
    user?: Userx;
    enabled?: boolean;
    isHealthy?: boolean;
    accessPoint?: AccessPoint;
    sensors?: Array<Sensor>;
    _new?: boolean;
}

