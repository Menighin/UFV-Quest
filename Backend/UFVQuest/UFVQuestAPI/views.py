from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
from django.utils import timezone
from django.core import serializers
from UFVQuestAPI.models import User, GoToAndAnswer, QuestType, Quest, SeekAndAnswer, UserAttemptsQuest
from django.db import IntegrityError
from django.core.serializers.json import DjangoJSONEncoder
from django.forms.models import model_to_dict
from django.db.models import Sum

import random, json, datetime, auth, ufvquest_utils


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
		
		data['name'] = u.name
		data['facebook_id'] = u.facebook_id
		data['api_key'] = u.api_key
		data['energy_left'] = u.energy_left
		data['gender'] = u.gender
		data['avatar'] = u.avatar
	except IntegrityError as e:
		uAux = User.objects.get(facebook_id = request.POST['facebook_id'])
		
		data['status'] = -7
		data['message'] = str(e)
		data['name'] = uAux.name
		data['facebook_id'] = uAux.facebook_id
		data['api_key'] = uAux.api_key
		data['energy_left'] = uAux.energy_left
		data['gender'] = uAux.gender
		data['avatar'] = uAux.avatar
		data['points'] = UserAttemptsQuest.objects.filter(user = uAux, solved = True).aggregate(Sum('points_won'))['points_won__sum']

		try:
			last_added_quest = Quest.objects.filter(created_by = uAux).order_by('-created_on')[0].created_on
			data['time_since_last_quest'] = (timezone.now() - last_added_quest).days
		except Exception as e:
			data['time_since_last_quest'] = 9999
	except Exception as e:
		data['status'] = 0
		data['message'] = str(e)

	return HttpResponse(json.dumps(data, cls=DjangoJSONEncoder), content_type="application/json")


@csrf_exempt
def getActiveQuests(request):
	data = {}
	data['status'] = 1

	try:
		data['quests'] = list()

		for model in SeekAndAnswer.objects.all():
			if(model.expiration_date >= timezone.now() or model.diary == True):
				data['quests'].append(model_to_dict(model))
				data['quests'][-1]['type'] = "saa"

		for model in GoToAndAnswer.objects.all():
			if(model.expiration_date >= timezone.now() or model.diary == True):
				data['quests'].append(model_to_dict(model))
				data['quests'][-1]['type'] = "gtaa"

	except Exception as e:
		data['status'] = 0
		data['message'] = str(e)
	
	return HttpResponse(json.dumps(data, cls=DjangoJSONEncoder), content_type="application/json")


@csrf_exempt
def getQuestIStillCanComplete(request):
	data = {}
	data['status'] = 1
	
	if(auth.authorize(request.POST.get('facebook_id', "0a"), request.POST.get('api_key', ""))):
		data['quests'] = list()
		
		try:
			user = User.objects.get(facebook_id = request.POST['facebook_id'])	

			for model in SeekAndAnswer.objects.all():
				if model.expiration_date >= timezone.now() or model.diary == True:
					successful_attempt = UserAttemptsQuest.objects.filter(user = user, solved = True, quest = model.quest_ptr)
					fail_attempts = UserAttemptsQuest.objects.filter(user = user, solved = False, quest = model.quest_ptr).order_by('-timestamp')			
					
					total_successful = UserAttemptsQuest.objects.filter(solved = True, quest = model.quest_ptr).count()	
					total_fail = UserAttemptsQuest.objects.filter(solved = False, quest = model.quest_ptr).count()
					total_quests = total_successful + total_fail if total_successful + total_fail > 0 else 1					

					tried_today = False
					if fail_attempts:
						last_fail = fail_attempts[0]
						if last_fail.timestamp.date() == timezone.now().date(): 
							tried_today = True
					
					if not successful_attempt and not tried_today:
						data['quests'].append(model_to_dict(model))
						data['quests'][-1]['type'] = "saa"
						points = int(model.quest_ptr.points - (model.quest_ptr.points * 0.05 * fail_attempts.count()))
						data['quests'][-1]['points'] = points if points > 10 else 10
						data['quests'][-1]['percent_loss'] = 5 * fail_attempts.count()
						data['quests'][-1]['success_rate'] = (total_successful*100)/(total_quests)

			for model in GoToAndAnswer.objects.all():
				if model.expiration_date >= timezone.now() or model.diary == True:
					successful_attempt = UserAttemptsQuest.objects.filter(user = user, solved = True, quest = model.quest_ptr)
					fail_attempts = UserAttemptsQuest.objects.filter(user = user, solved = False, quest = model.quest_ptr)
					
					total_successful = UserAttemptsQuest.objects.filter(solved = True, quest = model.quest_ptr).count()	
					total_fail = UserAttemptsQuest.objects.filter(solved = False, quest = model.quest_ptr).count()	
					total_quests = total_successful + total_fail if total_successful + total_fail > 0 else 1					

					tried_today = False
					if fail_attempts:
						last_fail = fail_attempts[0]
						if last_fail.timestamp.date() == timezone.now().date(): 
							tried_today = True

					if not successful_attempt and not tried_today:
						data['quests'].append(model_to_dict(model))
						data['quests'][-1]['type'] = "gtaa"
						points = int(model.quest_ptr.points - (model.quest_ptr.points * 0.05 * fail_attempts.count()))
						data['quests'][-1]['points'] = points if points > 10 else 10
						data['quests'][-1]['percent_loss'] = 5 * fail_attempts.count()
						data['quests'][-1]['success_rate'] = (total_successful*100)/(total_quests)

		except Exception as e:
			data['status'] = -1
			data['message'] = str(e)


	else:
		data['status'] = -77
		data['message'] = "User not authorized"

	return HttpResponse(json.dumps(data, cls=DjangoJSONEncoder), content_type="application/json")


@csrf_exempt
def getQuestTypes(request):
	data = {}
	data['status'] = 1

	try:
		data['questTypes'] = json.loads(serializers.serialize("json", QuestType.objects.all()))
	except Exception as e:
		data['status'] = 0
		data['message'] = str(e)

	return HttpResponse(json.dumps(data), content_type="application/json")


@csrf_exempt
def getUserRanking(request):
	data = {}
	data['status'] = 1
	auxTotal = list()
	auxWeek = list()
	
	try:
		total = UserAttemptsQuest.objects.filter(solved = True).values('user').annotate(points_sum=Sum('points_won'))
		week = UserAttemptsQuest.objects.filter(solved = True, timestamp__range = [timezone.now() - datetime.timedelta(days=7), timezone.now()] ).values('user').annotate(points_sum=Sum('points_won'))
	
		for user in total:
			auxTotal.append({'points': user['points_sum'], 'name': User.objects.get(id = user['user']).name, 'avatar' :  User.objects.get(id = user['user']).avatar})

		for user in week:
			auxWeek.append({'points': user['points_sum'], 'name': User.objects.get(id = user['user']).name, 'avatar' :  User.objects.get(id = user['user']).avatar})

		data['total'] = sorted(auxTotal, key = lambda k: -k['points'])
		data['week'] = sorted(auxWeek, key = lambda k: -k['points'])
	except Exception as e:
		data['status'] = 0
		data['message'] = str(e)

	return HttpResponse(json.dumps(data, cls=DjangoJSONEncoder), content_type="application/json")


@csrf_exempt
def getQuestHistory(request):
	data = {}
	data['status'] = 1
	data['history'] = list()
	
	if(auth.authorize(request.POST.get('facebook_id', "0a"), request.POST.get('api_key', ""))):
		try:
			user = User.objects.get(facebook_id = request.POST['facebook_id'])
			attempts = UserAttemptsQuest.objects.filter(user=user).order_by('-timestamp')

			for a in attempts:
				data['history'].append({'solved': a.solved, 'points': a.points_won, 'timestamp': a.timestamp.isoformat(), 
										'quest_title': a.quest.title, 'quest_type': a.quest.quest_type.alias, 'quest_description': a.quest.description})
			
		except Exception as e:
			data['status'] = 0
			data['message'] = str(e)
	else:
		data['status'] = -77
		data['message'] = "User not authorized"

	return HttpResponse(json.dumps(data), content_type="application/json")

	
@csrf_exempt
def createQuestGoToAndAnswer(request):
	data = {}
	data['status'] = 1
	
	if(auth.authorize(request.POST.get('facebook_id', "0a"), request.POST.get('api_key', ""))):
	
		quest = GoToAndAnswer()
	
		try:
			quest.title           = request.POST['title']
			quest.description     = request.POST['description']
			quest.place_name      = request.POST['place_name']
			quest.latitude        = request.POST['latitude']
			quest.longitude       = request.POST['longitude']
			quest.points          = int(ufvquest_utils.distance_to_centro_de_vivencia(request.POST['latitude'], request.POST['longitude']) * 0.5 + 25)
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

	return HttpResponse(json.dumps(data), content_type="application/json")


@csrf_exempt
def createQuestSeekAndAnswer(request):
	data = {}
	data['status'] = 1
	
	if(auth.authorize(request.POST.get('facebook_id', "0a"), request.POST.get('api_key', ""))):
	
		quest = SeekAndAnswer()
	
		try:
			quest.title           = request.POST['title']
			quest.description     = request.POST['description']
			quest.place_name      = request.POST['place_name']
			quest.latitude        = request.POST['latitude']
			quest.longitude       = request.POST['longitude']
			quest.points          = int(ufvquest_utils.distance_to_centro_de_vivencia(request.POST['latitude'], request.POST['longitude']) * 0.5 + 25)
			quest.created_on      = timezone.now()
			quest.expiration_date = timezone.now() + datetime.timedelta(days=7)
			quest.diary			  = False
			quest.created_by	  = User.objects.get(facebook_id = request.POST['facebook_id'])
			quest.quest_type	  = QuestType.objects.get(id=2)	

			quest.question        = request.POST['question']
			quest.answer          = request.POST['answer']
			quest.save()
		except Exception as e:
			data['status'] = 0
			data['message'] = str(e)

	else:
		data['status'] = -77
		data['message'] = "User not authorized"

	return HttpResponse(json.dumps(data), content_type="application/json")


@csrf_exempt
def tryQuest(request):
	data = {}
	data['status'] = 1

	if auth.authorize(request.POST.get('facebook_id', "0a"), request.POST.get('api_key', "")):

		attempt = UserAttemptsQuest()

		try:
			attempt.user = User.objects.get(facebook_id = request.POST['facebook_id'])
			attempt.quest = Quest.objects.get(id = request.POST['quest_id'])
			attempt.timestamp = timezone.now()

			if request.POST['quest_type'] == "gtaa":
				quest = GoToAndAnswer.objects.get(quest_ptr = attempt.quest)
				attempt.solved = True if str(quest.correct_answer) == request.POST['answer'] else False				
			elif request.POST['quest_type'] == "saa":
				quest = SeekAndAnswer.objects.get(quest_ptr = attempt.quest)
				attempt.solved = False
				for correct_answer in quest.answer.split(','):
					if correct_answer == request.POST['answer']:  
						attempt.solved = True

			previous_attempts = UserAttemptsQuest.objects.filter(user = attempt.user, quest = attempt.quest)

			points_won = int(attempt.quest.points - (attempt.quest.points * 0.05 * previous_attempts.count()))			
		
			attempt.points_won = points_won if points_won > 10 else 10

			data['status'] = 1 if attempt.solved == True else 2
			data['points_won'] = points_won

			attempt.save()
			
		except Exception as e:
			data['status'] = 0
			data['message'] = str(e)
		

	else:
		data['status'] = -77
		data['message'] = "User not authorized"

	return HttpResponse(json.dumps(data), content_type="application/json")


@csrf_exempt
def spendEnergy(request):
	data = {}
	data['status'] = 1

	if(auth.authorize(request.POST.get('facebook_id', "0a"), request.POST.get('api_key', ""))):

		try:
			user = User.objects.get(facebook_id = request.POST['facebook_id'])
			user.energy_left = user.energy_left - 1
			
			if (user.energy_left < 0):
				user.energy_left = 0
				data['message'] = "You already have 0 energy"

			user.save()
			
		except Exception as e:
			data['status'] = 0
			data['message'] = str(e)
	else:
		data['status'] = -77
		data['message'] = "User not authorized"

	return HttpResponse(json.dumps(data), content_type="application/json")
