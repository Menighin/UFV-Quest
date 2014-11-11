from django.db import models

class User(models.Model):
	facebook_id = models.CharField(max_length=200, unique=True)
	name = models.CharField(max_length=150)
	gender = models.CharField(max_length=20)
	avatar = models.CharField(max_length=100)
	api_key = models.CharField(max_length=200)
	energy_left = models.IntegerField(default=0)
	last_seen = models.DateTimeField('last seen')


class QuestType(models.Model):
	name = models.CharField(max_length=50)
	description = models.CharField(max_length=280)
	html = models.CharField(max_length=3000)
	alias = models.CharField(max_length=10, null=True)


class Quest(models.Model):
	title = models.CharField(max_length=120)
	description = models.CharField(max_length=200)
	place_name = models.CharField(max_length=100, null=True)
	latitude = models.FloatField(default=0.0)
	longitude = models.FloatField(default=0.0)
	points = models.IntegerField(default=0)
	created_on = models.DateTimeField('created on', auto_now_add=True)
	expiration_date = models.DateTimeField(null=True)
	diary = models.BooleanField(default=False)
	quest_type = models.ForeignKey(QuestType)
	created_by = models.ForeignKey(User)
	

class GoToAndAnswer(Quest):
	question = models.CharField(max_length=180)
	answer1 = models.CharField(max_length=100)
	answer2 = models.CharField(max_length=100)
	answer3 = models.CharField(max_length=100)
	answer4 = models.CharField(max_length=100)
	correct_answer = models.IntegerField(default=0)


class SeekAndAnswer(Quest):
	question = models.CharField(max_length=180)
	answer = models.CharField(max_length=300)


class UserAttemptsQuest (models.Model):
	user = models.ForeignKey(User)
	quest = models.ForeignKey(Quest)
	timestamp = models.DateTimeField(auto_now_add=True)
	solved = models.BooleanField(default=False)
	points_won = models.IntegerField()
