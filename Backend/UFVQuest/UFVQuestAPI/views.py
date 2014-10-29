from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
from django.utils import timezone
from django.core import serializers
from UFVQuestAPI.models import User, GoToAndAnswer, QuestType
from django.db import IntegrityError

import random, json, datetime, auth


def index(request):
	return HttpResponse("Hello, world. You're at the polls index.")


def user(request, user_id):
	some_data_to_dump = {
		'value1': '1',
		'value2': 'menighin'
	}
	data = serializers.serialize('json', [User.objects.get(pk=user_id)])
	return HttpResponse(data)


@csrf_exempt
def createUser(request):
	u = User()
	data = {}
	data['status'] = 1

	try:
		u.facebook_id = request.POST['facebook_id']
		u.name        = request.POST['name']
		u.gender      = request.POST['gender']
		u.avatar      = "http://graph.facebook.com/" + request.POST['facebook_id'] + "/picture?type=large"
		u.api_key     = hex(random.getrandbits(192)).lstrip('0x').rstrip('L')
		u.energy_left = 5
		u.last_seen   = timezone.now()
		u.save()
	except IntegrityError as e:
		data['status'] = -7
		data['message'] = str(e)
	except Exception as e:
		data['status'] = 0
		data['message'] = str(e)

	return HttpResponse(json.dumps(data))


@csrf_exempt
def createQuestGoToAndAnswer(request):
	data = {}
	data['status'] = 1 

	if(auth.authorize(request.POST['facebook_id'], request.POST['api_key'])):
	
		quest = GoToAndAnswer()
	
		try:
			quest.title           = request.POST['title']
			quest.description     = request.POST['description']
			quest.place_name      = request.POST['place_name']
			quest.latitude        = request.POST['latitude']
			quest.longitude       = request.POST['longitude']
			quest.points          = request.POST['points']
			quest.created_on      = timezone.now()
			quest.expiration_date = timezone.now() + datetime.timedelta(days=7)
			quest.diary			  = False
			quest.created_by	  = User.objects.get(facebook_id = request.POST['facebook_id'])
			quest.quest_type	  = QuestType.objects.get(id=1)	

			quest.question        = request.POST['question']
			quest.answer1         = request.POST['answer1']
			quest.answer2         = request.POST['answer2']
			quest.answer3         = request.POST['answer3']
			quest.answer4         = request.POST['answer4']
			quest.correct_answer  = request.POST['correct_answer']
			quest.save()
		except Exception as e:
			data['status'] = 0
			data['message'] = str(e)

	else:
		data['status'] = -77
		data['message'] = "User not authorized"

	return HttpResponse(json.dumps(data))
