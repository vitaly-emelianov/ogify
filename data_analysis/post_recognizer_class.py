KEY_WORDS = ['купить', 'продать', 'искать']
MAX_POST_LENGTH = 20


class TextProcessor:
	def __init__(self, input_path):
		self.input_path = input_path
		
	def read(self):
	    with open(self.input_path) as f:
	        for line in f:
			    text = line[:(line.rindex(']') + 1)]
			self.posts = json.loads(text)
			
	def make_tokens(self):
		#build tokenizer
		tokenizer = SuperTokenizer()

		#open file for writing tokens
		with open(self.output_path, 'w') as post_output:
			for post_index, post in enumerate(self.posts):
				#tokenizing current post
				tokens = tokenizer.tokenize_text(post['text'])

				#writing tokens frim current post to file
				yield post_index + ','.join(tokens).encode('utf-8') 


class PostRecognizer():
    def __init__(text_path, token_path):
        self.text_path = text_path
        self.token_path = token_path
        
    def read_data(path):
        data = []  
        with open(path, 'r') as output:
            for line in output:
                data.append(line)
        return data
        
    def preprocess():
        self.tokens = read_data(self.token_path)
        self.text = read_data(self.text_path)
        
        self.relevant_posts = []
        for token in self.tokens:
            token_index = int(token.split(' ')[0])
            token_words = token.split(' ')[1].split(',')
            for word in KEY_WORDS:
                if word in token_words and len(token_words) < MAX_POST_LENGTH:
                    self.relevant_posts.append(posts[token_index])
                    
    def get_precision():
        return len(self.relevant_posts) * 1.0 / len(self.text)
        
    def evaluate(post_path):
        processor = TextProcessor(post_path)
        processor.read()
        tokens_post = processor.make_tokens()
        
        for word in KEY_WORDS:
            if word in tokens_post and len(tokens_post) < MAX_POST_LENGTH:
                token_index = int(token_post.split(' ')[0])
                self.relevant_posts.append(posts[token_index])
        