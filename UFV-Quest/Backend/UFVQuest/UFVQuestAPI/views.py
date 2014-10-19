from django.shortcuts import render

from django.http import HttpResponse

#from django.utils import simplejson
from django.core import serializers

from ufv_quest_api.models import User



def index(request):
	return HttpResponse("Hello, world. You're at the polls index.")


def user(request, user_id):
	some_data_to_dump = {
		'value1': '1',
		'value2': 'menighin'
	}
	#data = simplejson.dumps(some_data_to_dump)
	data = serializers.serialize('json', [User.objects.get(pk=user_id)])
	return HttpResponse(data)
	#return HttpResponse("You're seeing user %s." % user_id)
