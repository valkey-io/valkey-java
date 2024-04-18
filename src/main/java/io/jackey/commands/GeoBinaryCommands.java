package io.jackey.commands;

import java.util.List;
import java.util.Map;

import io.jackey.GeoCoordinate;
import io.jackey.args.GeoUnit;
import io.jackey.params.GeoAddParams;
import io.jackey.params.GeoRadiusParam;
import io.jackey.params.GeoRadiusStoreParam;
import io.jackey.params.GeoSearchParam;
import io.jackey.resps.GeoRadiusResponse;

public interface GeoBinaryCommands {

  long geoadd(byte[] key, double longitude, double latitude, byte[] member);

  long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap);

  long geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap);

  Double geodist(byte[] key, byte[] member1, byte[] member2);

  Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit);

  List<byte[]> geohash(byte[] key, byte[]... members);

  List<GeoCoordinate> geopos(byte[] key, byte[]... members);

  List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius,
      GeoUnit unit);

  List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude,
      double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius,
      GeoUnit unit, GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude,
      double radius, GeoUnit unit, GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit,
      GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius,
      GeoUnit unit, GeoRadiusParam param);

  long georadiusStore(byte[] key, double longitude, double latitude, double radius, GeoUnit unit,
      GeoRadiusParam param, GeoRadiusStoreParam storeParam);

  long georadiusByMemberStore(byte[] key, byte[] member, double radius, GeoUnit unit,
      GeoRadiusParam param, GeoRadiusStoreParam storeParam);
  
  List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double radius, GeoUnit unit);

  List<GeoRadiusResponse> geosearch(byte[] key, GeoCoordinate coord, double radius, GeoUnit unit);

  List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double width, double height, GeoUnit unit);

  List<GeoRadiusResponse> geosearch(byte[] key, GeoCoordinate coord, double width, double height, GeoUnit unit);

  List<GeoRadiusResponse> geosearch(byte[] key, GeoSearchParam params);

  long geosearchStore(byte[] dest, byte[] src, byte[] member, double radius, GeoUnit unit);

  long geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double radius, GeoUnit unit);

  long geosearchStore(byte[] dest, byte[] src, byte[] member, double width, double height, GeoUnit unit);

  long geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double width, double height, GeoUnit unit);

  long geosearchStore(byte[] dest, byte[] src, GeoSearchParam params);

  long geosearchStoreStoreDist(byte[] dest, byte[] src, GeoSearchParam params);
}
