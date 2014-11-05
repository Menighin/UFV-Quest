from math import radians, cos, sin, asin, sqrt


#Calculate distance in meters between the Lat and Lon passed and the Centro de Vivencia (-20.761717, -42.869958)
def distance_to_centro_de_vivencia(lat, lon):
	lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, -20.761717, -42.869958])
    # haversine formula 
    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a)) 
    km = 6367 * c
    return (km * 1000)
