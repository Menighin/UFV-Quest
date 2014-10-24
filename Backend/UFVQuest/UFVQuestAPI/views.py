from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
from django.utils import timezone
from django.core import serializers
from UFVQuestAPI.models import User

import random, json


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

@csrf_exempt
def createUser(request):
	u = User()
	data = {}
	data['status'] = 1

	try:
		u.facebook_id = request.POST['facebook_id']
		u.name        = request.POST['name']
		u.gender      = request.POST['gender']
		u.avatar      = request.POST['avatar']
		u.api_key     = random.getrandbits(128)
		u.energy_left = 5
		u.last_seen   = timezone.now()
		u.save()
	except Exception as e:
		data['status'] = 0

	return HttpResponse(json.dumps(data))
