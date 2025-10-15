# Problem D: Plan Out

`25 points`

Terry the tech-lead is planning an outing for her team of N coders. There will be M fun outdoor activities distributed
over two days, with the following requirements:

- Each activity must be scheduled on either Day 1 or Day 2
- Each activity i has exactly two distinct participants, coders Ai and Bi.
- No pair of coders is involved in more than one activity together.

On each given day:

- The total cost that any given coder incurs on that day is the square of the number of activities they do on that day.
- The total cost for that day is the sum of the cost incurred by all coders that day.

The total cost of the outing is the sum of costs across both days. Please help Terry find any assignment of activities
to days so that the total cost of the outing is minimized.

## Constraints

- 1≤T≤65
- 2≤N≤200,000
- 1≤M≤200,000
- 1≤Ai,Bi≤N; Ai≠Bi

In any given test case, no unordered pair (Ai,Bi) appears more than once.

## Input Format

Input begins with an integer T, the number of test cases. The first line of each case contains two space-separated
integers N and M. Then, M lines follow, the i-th of which contains two space-separated integers Ai and Bi.

## Output Format

For the i-th case, print "Case #i:" followed by the minimum possible total cost of the outing, followed by a space,
followed by a length-M string where the j-th character is '1' if activity j is scheduled for Day 1, or '2' if it's
scheduled for Day 2.

## Sample Explanation

The first test case has 5 coders and 4 activities. If the first activity is on the second day, and the last three
activities are on the first day, the total cost for the first day will be 1^2+2^2+1^2+1^2+1^2=8, and the total cost
for the second day will be 1^2+1^2+0^2+0^2+0^2=2. The total cost of the outing is 8+2=10.

## Sample Input

```
5
5 4
1 2
4 2
1 5
2 3
6 8
1 5
6 3
2 3
1 2
4 3
2 6
5 2
2 4
5 6
4 5
1 3
1 5
3 2
2 1
1 4
3 1
3 1
8 8
2 4
4 5
2 3
1 6
6 7
3 5
3 4
6 8
```

## Sample Output

```
Case #1: 10 2111
Case #2: 26 21212211
Case #3: 16 112212
Case #4: 2 1
Case #5: 22 21112211
```